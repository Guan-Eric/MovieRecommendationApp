package com.example.MovieRecommendationBackend.service;

import com.example.MovieRecommendationBackend.entity.*;
import com.example.MovieRecommendationBackend.repository.UserMovieRepository;
import com.example.MovieRecommendationBackend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.MovieRecommendationBackend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Service
public class MovieService {

    @Autowired
    UserMovieRepository userMovieRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${openai.apiKey}")
    String apiKey;

    public List<UserMovie> getUserMoviesByStatusId(HttpServletRequest request, String status) {
        int statusId = switch (status) {
            case "towatch" -> 1;
            case "seen" -> 2;
            case "tonotwatch" -> 3;
            default -> throw new IllegalArgumentException("Invalid status: " + status);};

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "userId".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> userMovieRepository.findByUserIdAndStatusId(Integer.parseInt(cookie.getValue()), statusId))
                .orElseGet(() -> {
                    System.out.println("No userId cookie found");
                    return new ArrayList<>();
                });
    }

    public ResponseEntity<?> updateUserMovieStatus(HttpServletRequest request, MovieInput movieInput) {
        String movieName = movieInput.getMovieName();
        String description = movieInput.getDescription();
        String date = movieInput.getDate();
        String statusName = movieInput.getStatusName();
        int rating = movieInput.getRating();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    Movie movie = movieRepository.findByMovieNameAndDate(movieName, date);
                    Optional<UserMovie> optionalUserMovie = userMovieRepository.findById(new UserMovieId(Integer.valueOf(cookie.getValue()), movie.getId()));

                    UserMovie userMovie;
                    if (optionalUserMovie.isPresent()) {
                        userMovie = optionalUserMovie.get();
                    }
                    else {
                        throw new RuntimeException("UserMovie not found");
                    }
                    userMovie.setRating(rating);

                    int statusId = 0;
                    switch (statusName) {
                        case "towatch" -> statusId = 1;
                        case "seen" -> statusId = 2;
                        case "tonotwatch" -> statusId = 3;
                        default -> throw new IllegalArgumentException("Invalid status: " + statusName);
                    }
                    Status status = new Status();
                    status.setStatusId(statusId);
                    userMovie.setStatus(status);


                    userMovieRepository.save(userMovie);
                    return ResponseEntity.ok(Map.of("message", "Edit movie successful"));
                }
            }
        }
        throw new RuntimeException("Failed to edit movie");
    }

    public ResponseEntity<?> saveUserMovie(HttpServletRequest request, MovieInput movieInput) {
        String movieName = movieInput.getMovieName();
        String description = movieInput.getDescription();
        String date = movieInput.getDate();
        String statusName = movieInput.getStatusName();
        int rating = movieInput.getRating();

        Movie movie = new Movie();
        if (movieRepository.existsByMovieNameAndDate(movieName, date)) {
            movie = movieRepository.findByMovieNameAndDate(movieName, date);
        }
        else {
            movie.setMovieName(movieName);
            movie.setDate(date);
            movie.setDescription(description);
            movie = movieRepository.save(movie);
        }

        int statusId = 0;
        switch (statusName) {
            case "towatch" -> statusId = 1;
            case "seen" -> statusId = 2;
            case "tonotwatch" -> statusId = 3;
            default -> throw new IllegalArgumentException("Invalid status: " + statusName);
        }
        Status status = new Status();
        status.setStatusId(statusId);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    UserMovie userMovie = new UserMovie(
                            userRepository.findById(Integer.valueOf(cookie.getValue())).get(),
                            movie,
                            status,
                            rating
                    );
                    UserMovieId userMovieId = new UserMovieId(userMovie.getUser().getId(), movie.getId());
                    userMovie.setId(userMovieId);
                    userMovieRepository.save(userMovie);
                    return ResponseEntity.ok(Map.of("message","Add movie successful"));
                }
            }
        }
        throw new RuntimeException("Failed to add movie");
    }

    public ResponseEntity<?> deleteUserMovie(HttpServletRequest request, MovieInput movieInput) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    UserMovieId userMovieId = new UserMovieId(
                            userRepository.findById(Integer.valueOf(cookie.getValue())).get().getId(),
                            movieRepository.findByMovieNameAndDate(movieInput.getMovieName(), movieInput.getDate()).getId());
                    userMovieRepository.deleteById(userMovieId);
                    return ResponseEntity.ok(Map.of("message","Remove movie successful"));
                }
            }
        }
        throw new RuntimeException("Failed to remove movie");
    }

    public List<Movie> generateRecommendations(HttpServletRequest request, List<String> constraints) {

        List<UserMovie> seenUserMovies = this.getUserMoviesByStatusId(request, "seen");
        List<UserMovie> unwantedUserMovies = this.getUserMoviesByStatusId(request, "tonotwatch");
        List<UserMovie> watchlistUserMovies = this.getUserMoviesByStatusId(request, "towatch");

        List<String> seenMovies = new ArrayList<>();
        if (seenUserMovies != null) {
            for (UserMovie seenUserMovie : seenUserMovies) {
                seenMovies.add(seenUserMovie.getMovie().getMovieName() + " rating: " + seenUserMovie.getRating());
            }
        }

        List<String> unwantedMovies = new ArrayList<>();
        if (unwantedUserMovies != null) {
            for (UserMovie unwantedUserMovie : unwantedUserMovies) {
                unwantedMovies.add(unwantedUserMovie.getMovie().getMovieName());
            }
        }

        List<String> watchlistMovies = new ArrayList<>();
        if (watchlistUserMovies != null) {
            for (UserMovie watchUserMovie : watchlistUserMovies) {
                watchlistMovies.add(watchUserMovie.getMovie().getMovieName());
            }
        }

        JsonObject jsonObject = callOpenAI(apiKey, seenMovies, unwantedMovies, watchlistMovies, constraints);
        return convertRecommendationsToUserMovies(jsonObject);
    }

    public JsonObject callOpenAI(String apiKey, List<String> seenMovies, List<String> unwantedMovies, List<String> watchlistMovies, List<String> otherConstraints) {
        /*
        Sample inputs:
        List<String> seenMovies = List.of("Blade Runner", "The Matrix");
        List<String> unwantedMovies = List.of("Inception");
        List<String> watchlistMovies = List.of("Interstellar");
        List<String> otherConstraints = List.of("Prefer movies rated above 8 on IMDb", "Genre: Sci-fi", "Theme: Romance");
         */
        String model = "gpt-4-turbo";
        String url = "https://api.openai.com/v1/chat/completions";
        int maxRetries = 5;  // Define the maximum number of retries
        int attempts = 0;
        HttpURLConnection connection = null;

        while (attempts < maxRetries) {
            try {
                // Set up http connection using url and api key
                URL urlObject = new URL(url);
                connection = (HttpURLConnection) urlObject.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Constructing json payload
                JsonObject root = new JsonObject();
                root.addProperty("model", model);

                JsonArray messages = new JsonArray();

                JsonObject systemMessage = new JsonObject();
                systemMessage.addProperty("role", "system");
                systemMessage.addProperty("content", "You are a movie recommendation engine. Please provide movie recommendations in JSON format with the following details: name, year, description.");
                messages.add(systemMessage);

                JsonObject userMessage = new JsonObject();
                userMessage.addProperty("role", "user");

                // Create context json

                // Seen movies will be strings of the form "Movie name (year) (my rating: X/5)
                JsonObject context = new JsonObject();
                JsonArray seenArray = new JsonArray();
                if (seenMovies != null) seenMovies.forEach(seenArray::add);
                context.add("seenMovies", seenArray);


                // Just strings Movie name (year)
                JsonArray unwantedArray = new JsonArray();
                if (unwantedMovies != null) unwantedMovies.forEach(unwantedArray::add);
                context.add("unwantedMovies", unwantedArray);

                // Just strings Movie name (year)
                JsonArray watchlistArray = new JsonArray();
                if (watchlistMovies != null) watchlistMovies.forEach(watchlistArray::add);
                context.add("watchlistMovies", watchlistArray);

                // This is just a list of arbitrary "preference" strings e.g., "Prefer movies rated above 8 on IMDb", "Genre: Sci-fi", "Theme: Romance"
                JsonArray constraintsArray = new JsonArray();
                if (otherConstraints != null) otherConstraints.forEach(constraintsArray::add);
                context.add("otherConstraints", constraintsArray);

                String prompt = "Suggest 10 new movies excluding the ones I've seen or don't want to see, and not already on my watchlist, considering other constraints.";

                userMessage.addProperty("content", "Context: " + context.toString() + "\n" + prompt);
                messages.add(userMessage);
                root.add("messages", messages);

                // Stream of bytes where data can be sent, openai api in this case
                OutputStream os = connection.getOutputStream();
                // Convert payload to bytes (format required to send over network)
                byte[] input = root.toString().getBytes(StandardCharsets.UTF_8);
                // Write to output stream
                os.write(input, 0, input.length);

                // Read the response from openai, read input stream from api response
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                // Parse the response
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();

                // Convert response to a json
                JsonObject responseObj = JsonParser.parseString(response.toString()).getAsJsonObject();
                // responseObj example: {"id":"chatcmpl-9EIHdsQnpA4hYZ4EZbFA6zqrmbuur","object":"chat.completion","created":1713193901,"model":"gpt-3.5-turbo-0125","choices":[{"index":0,"message":{"role":"assistant","content":"{\n  \"movieRecommendations\": [\n    {\n      \"name\": \"Eternal Sunshine of the Spotless Mind\",\n      \"year\": 2004,\n      \"description\": \"A couple undergoes a medical procedure to erase each other from their memories.\"\n    },\n    {\n      \"name\": \"Her\",\n      \"year\": 2013,\n      \"description\": \"A lonely writer develops an unlikely relationship with an operating system designed to meet his every need.\"\n    },\n    {\n      \"name\": \"Gravity\",\n      \"year\": 2013,\n      \"description\": \"Two astronauts stranded in space must work together to survive.\"\n    },\n    {\n      \"name\": \"The Shape of Water\",\n      \"year\": 2017,\n      \"description\": \"A mute janitor forms a unique relationship with an amphibious creature in a high-security government lab.\"\n    },\n    {\n      \"name\": \"Ex Machina\",\n      \"year\": 2014,\n      \"description\": \"A programmer is selected to participate in an experiment in synthetic intelligence by evaluating the human qualities of a highly advanced humanoid A.I.\"\n    },\n    {\n      \"name\": \"Arrival\",\n      \"year\": 2016,\n      \"description\": \"A linguist is recruited by the military to assist in translating alien communications.\"\n    },\n    {\n      \"name\": \"Upstream Color\",\n      \"year\": 2013,\n      \"description\": \"A man and woman are drawn together, entangled in the life cycle of an ageless organism.\"\n    },\n    {\n      \"name\": \"Annihilation\",\n      \"year\": 2018,\n      \"description\": \"A biologist signs up for a dangerous, secret expedition into a mysterious zone where the laws of nature don't apply.\"\n    },\n    {\n      \"name\": \"500 Days of Summer\",\n      \"year\": 2009,\n      \"description\": \"An offbeat romantic comedy about a woman who doesn't believe true love exists, and the young man who falls for her.\"\n    },\n    {\n      \"name\": \"Moon\",\n      \"year\": 2009,\n      \"description\": \"Astronaut Sam Bell has a quintessentially personal encounter toward the end of his three-year stint on the moon.\"\n    }\n  ]\n}"},"logprobs":null,"finish_reason":"stop"}],"usage":{"prompt_tokens":116,"completion_tokens":466,"total_tokens":582},"system_fingerprint":"fp_c2295e73ad"}
                // Extract choices array
                JsonArray choices = responseObj.getAsJsonArray("choices");
                // if there are choices
                if (choices.size() > 0) {
                    JsonObject choice = choices.get(0).getAsJsonObject();
                    JsonObject messageObj = choice.getAsJsonObject("message");
                    if (messageObj.has("content")) {
                        String content = messageObj.get("content").getAsString();
                        content = content.replaceAll("```json|```", "").trim();
                        JsonReader reader = new JsonReader(new StringReader(content));
                        reader.setLenient(true); // lenient means accept malformed json
                        JsonObject movieData = JsonParser.parseReader(reader).getAsJsonObject();
                        JsonObject standardizedResponse = new JsonObject();
                        standardizedResponse.add("recommendations", movieData.entrySet().iterator().next().getValue());
                        return standardizedResponse;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (attempts == maxRetries - 1) {
                    JsonObject error = new JsonObject();
                    error.addProperty("error", "An error occurred: " + e.getMessage());
                    return error;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            attempts++;
        }
        JsonObject error = new JsonObject();
        error.addProperty("error", "Max retries reached without successful response.");
        return error;
    }

    public List<Movie> convertRecommendationsToUserMovies(JsonObject jsonObject) {

        JsonArray recommendations = jsonObject.getAsJsonArray("recommendations");

        List<Movie> movies = new ArrayList<>();

        for (JsonElement recommendation : recommendations) {
            JsonObject movieData = recommendation.getAsJsonObject();
            String name = movieData.get("name").getAsString();
            String year = movieData.get("year").getAsString();
            String description = movieData.get("description").getAsString();

            Movie movie = new Movie();
            movie.setMovieName(name);
            movie.setDate(year);
            movie.setDescription(description);

            movies.add(movie);
        }

        return movies;
    }
}
