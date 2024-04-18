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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        User user = new User();
        user.setId(1);

        when(movieRepository.existsByMovieNameAndDate("Inception", "2010")).thenReturn(false);
        when(movieRepository.findByMovieNameAndDate("Inception", "2010")).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMovieRepository.save(any(UserMovie.class))).thenReturn(new UserMovie());
        movieService.saveUserMovie(request, movieInput);
        UserMovie userMovie = new UserMovie();
        userMovie.setMovie(movie);
        movieInput.setStatusName("seen");

        when(movieRepository.findByMovieNameAndDate("Inception", "2010")).thenReturn(movie);
        when(userMovieRepository.findById(new UserMovieId(1, 1))).thenReturn(Optional.of(userMovie));

        ResponseEntity<?> responseEntity = movieService.updateUserMovieStatus(request, movieInput);

        assertEquals("Edit movie successful", ((Map<String, String>) responseEntity.getBody()).get("message"));
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

        when(movieRepository.existsByMovieNameAndDate("Inception", "2010")).thenReturn(false);
        when(movieRepository.findByMovieNameAndDate("Inception", "2010")).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMovieRepository.save(any(UserMovie.class))).thenReturn(new UserMovie());

        MovieInput movieInput = new MovieInput();
        movieInput.setMovieName("Inception");
        movieInput.setDate("2010");
        movieInput.setStatusName("towatch");
        movieInput.setRating(5);

        ResponseEntity<?> responseEntity = movieService.saveUserMovie(request, movieInput);

        assertEquals("Add movie successful", ((Map<String, String>) responseEntity.getBody()).get("message"));
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
    void testGenerateRecommendations() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        JsonObject mockResponse = new JsonObject();
        JsonArray recommendations = new JsonArray();
        JsonObject movie1 = new JsonObject();
        movie1.addProperty("name", "Interstellar");
        movie1.addProperty("year", "2014");
        movie1.addProperty("description", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.");
        recommendations.add(movie1);
        mockResponse.add("recommendations", recommendations);
        when(movieService.callOpenAI(any(), any(), any(), any())).thenReturn(mockResponse);

        List<String> constraints = new ArrayList<>();
        List<Movie> movieRecommendations = movieService.generateRecommendations(request, constraints);

        assertEquals(1, movieRecommendations.size());
        assertEquals("Interstellar", movieRecommendations.get(0).getMovieName());
        assertEquals("2014", movieRecommendations.get(0).getDate());
        assertEquals("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", movieRecommendations.get(0).getDescription());
    }
}