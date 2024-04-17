package com.example.MovieRecommendationBackend.service;

import com.example.MovieRecommendationBackend.entity.*;
import com.example.MovieRecommendationBackend.repository.MovieRepository;
import com.example.MovieRecommendationBackend.repository.UserMovieRepository;
import com.example.MovieRecommendationBackend.repository.UserRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class MovieServiceTest {

    @Mock
    private UserMovieRepository userMovieRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void testGetUserMoviesByStatusId() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});
        when(userMovieRepository.findByUserIdAndStatusId(1, 1))
                .thenReturn(Arrays.asList(new UserMovie(), new UserMovie()));

        List<UserMovie> userMovies = movieService.getUserMoviesByStatusId(request, "towatch");

        assertEquals(2, userMovies.size());
    }

    @Test
    void testUpdateUserMovieStatus() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});

        MovieInput movieInput = new MovieInput();
        movieInput.setMovieName("Inception");
        movieInput.setDate("2010");
        movieInput.setStatusName("towatch");
        movieInput.setRating(5);

        Movie movie = new Movie();
        movie.setMovieName("Inception");
        movie.setDate("2010");
        UserMovie userMovie = new UserMovie();
        userMovie.setMovie(movie);

        when(movieRepository.findByMovieNameAndDate("Inception", "2010")).thenReturn(movie);
        when(userMovieRepository.findById(new UserMovieId(1, movie.getId()))).thenReturn(Optional.of(userMovie));

        ResponseEntity<String> responseEntity = movieService.updateUserMovieStatus(request, movieInput);

        assertEquals("Edit movie successful", responseEntity.getBody());
    }

    @Test
    void testSaveUserMovie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});

        Movie movie = new Movie();
        movie.setMovieName("Inception");
        movie.setDate("2010");
        movie.setDescription("Description of Inception");
        movie.setId(1);

        User user = new User();
        user.setId(1);

        UserMovie userMovie = new UserMovie();
        userMovie.setMovie(movie);
        userMovie.setUser(user);

        when(movieRepository.existsByMovieNameAndDate("Inception", "2010")).thenReturn(false);
        when(movieRepository.findByMovieNameAndDate("Inception", "2010")).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(movie);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMovieRepository.save(userMovie)).thenReturn(userMovie);

        MovieInput movieInput = new MovieInput();
        movieInput.setMovieName("Inception");
        movieInput.setDate("2010");
        movieInput.setStatusName("towatch");
        movieInput.setRating(5);

        ResponseEntity<String> responseEntity = movieService.saveUserMovie(request, movieInput);

        assertEquals("Add movie successful", responseEntity.getBody());
    }

    @Test
    void testDeleteUserMovie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});

        MovieInput movieInput = new MovieInput();
        movieInput.setMovieName("Inception");
        movieInput.setDate("2010");

        Movie movie = new Movie();
        movie.setMovieName("Inception");
        movie.setDate("2010");

        when(movieRepository.findByMovieNameAndDate("Inception", "2010")).thenReturn(movie);
        when(userRepository.findById(1)).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> movieService.deleteUserMovie(request, movieInput));
    }


    @Test
    void testCallOpenAI() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});

        // Mock the dependencies for generating recommendations
        MovieService movieService = new MovieService();
        movieService.movieRepository = mock(MovieRepository.class);
        movieService.userMovieRepository = mock(UserMovieRepository.class);

        List<UserMovie> seenUserMovies = new ArrayList<>();
        // Add some seen movies
        seenUserMovies.add(createUserMovie("Inception", "2010"));
        seenUserMovies.add(createUserMovie("The Matrix", "1999"));

        // Mock the response of getUserMoviesByStatusId
        when(movieService.getUserMoviesByStatusId(request, "seen")).thenReturn(seenUserMovies);

        // Other necessary mock setups for unwanted and watchlist movies...

        List<String> constraints = new ArrayList<>();
        // Add constraints if needed

        // Mock the call to OpenAI API
        JsonObject mockResponse = new JsonObject();
        // Create a sample response similar to the actual API response
        // Adjust the response according to your test needs
        // For simplicity, let's assume the response contains movie recommendations
        JsonArray recommendations = new JsonArray();
        JsonObject movie1 = new JsonObject();
        movie1.addProperty("name", "Interstellar");
        movie1.addProperty("year", "2014");
        movie1.addProperty("description", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.");
        recommendations.add(movie1);

        JsonObject movie2 = new JsonObject();
        // Add more movies if needed

        mockResponse.add("recommendations", recommendations);

        when(movieService.callOpenAI(
                // Pass seen, unwanted, and watchlist movies
                List.of("Inception", "The Matrix"),
                List.of(),
                List.of(),
                // Pass constraints
                constraints
        )).thenReturn(mockResponse);

        List<Movie> movieRecommendations = movieService.generateRecommendations(request, constraints);

        // Verify the number of recommendations or any other assertions as needed
        assertEquals(1, movieRecommendations.size());
        assertEquals("Interstellar", movieRecommendations.get(0).getMovieName());
        assertEquals("2014", movieRecommendations.get(0).getDate());
        assertEquals("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", movieRecommendations.get(0).getDescription());
    }

    private UserMovie createUserMovie(String movieName, String date) {
        UserMovie userMovie = new UserMovie();
        Movie movie = new Movie();
        movie.setMovieName(movieName);
        movie.setDate(date);
        userMovie.setMovie(movie);
        return userMovie;
    }
}