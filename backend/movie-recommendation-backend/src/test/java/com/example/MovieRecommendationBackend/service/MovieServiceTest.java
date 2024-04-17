package com.example.MovieRecommendationBackend.service;

import com.example.MovieRecommendationBackend.entity.Movie;
import com.example.MovieRecommendationBackend.entity.User;
import com.example.MovieRecommendationBackend.entity.UserMovie;
import com.example.MovieRecommendationBackend.entity.UserMovieId;
import com.example.MovieRecommendationBackend.repository.MovieRepository;
import com.example.MovieRecommendationBackend.repository.UserMovieRepository;
import com.example.MovieRecommendationBackend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

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
        UserMovie userMovie = new UserMovie();
        userMovie.setId(new UserMovieId(1, 4));
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(new User()));
        when(userMovieRepository.save(userMovie)).thenReturn(userMovie);

        UserMovie updatedUserMovie = movieService.updateUserMovieStatus(request, userMovie);

        assertEquals(new UserMovieId(1, 4).getMovieId(), updatedUserMovie.getId().getMovieId());
        assertNotNull(updatedUserMovie);
    }

    //@Test
    void testGetUserMovie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});
        UserMovie userMovie = new UserMovie();
        userMovie.setId(new UserMovieId(1, 1));
        when(userMovieRepository.findById(new UserMovieId(1, 1))).thenReturn(java.util.Optional.of(userMovie));

        UserMovie retrievedUserMovie = movieService.getUserMovie(request, 1);

        assertNotNull(retrievedUserMovie);
        assertEquals(1, retrievedUserMovie.getId().getUserId());
        assertEquals(1, retrievedUserMovie.getId().getMovieId());
    }

    @Test
    void testSaveUserMovie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});
        Movie movie = new Movie();
        UserMovie userMovie = new UserMovie();
        userMovie.setId(new UserMovieId(1, 1));
        userMovie.setMovie(movie);
        when(movieRepository.existsByMovieNameAndDate(movie.getMovieName(), movie.getDate())).thenReturn(false);
        when(movieRepository.save(movie)).thenReturn(movie);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(new User()));

        ResponseEntity<String> responseEntity = movieService.saveUserMovie(request, userMovie);

        assertNotNull(responseEntity);
        assertEquals("Add movie successful", responseEntity.getBody());
    }

    @Test
    void testDeleteUserMovie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("userId", "1")});
        UserMovie userMovie = new UserMovie();
        userMovie.setId(new UserMovieId(1, 1));

        assertDoesNotThrow(() -> movieService.deleteUserMovie(request, userMovie));
    }

    @Test
    void testCallOpenAI() {
        // Implement test for callOpenAI method
    }
}