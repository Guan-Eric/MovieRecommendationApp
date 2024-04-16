package com.example.MovieRecommendationBackend.controller;

import com.example.MovieRecommendationBackend.entity.Movie;
import com.example.MovieRecommendationBackend.entity.User;
import com.example.MovieRecommendationBackend.entity.UserMovie;
import com.example.MovieRecommendationBackend.service.MovieService;
import com.example.MovieRecommendationBackend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User request, HttpServletResponse response){
        return userService.signUp(request, response);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody User request, HttpServletResponse response){
        return userService.login(request, response);
    }

    @GetMapping("/{status}")
    public List<UserMovie> getAllByStatusId(HttpServletRequest request, @PathVariable("status") String status) {
        return movieService.getUserMoviesByStatusId(request, status);
    }

    @PostMapping("/addmovie")
    public UserMovie addUserMovie(HttpServletRequest request, @RequestBody UserMovie userMovie) {
        return movieService.saveUserMovie(request, userMovie);
    }

    @GetMapping("/movie/{movieId}")
    public UserMovie getUserMovie(HttpServletRequest request, @PathVariable("movieId") int movieId) {
        return movieService.getUserMovie(request, movieId);
    }

    @PutMapping("/editmovie")
    public UserMovie updateUserMovieStatus(HttpServletRequest request, @RequestBody UserMovie userMovie) {
        return movieService.updateUserMovieStatus(request, userMovie);
    }

    @DeleteMapping("/removemovie")
    public void deleteUserMovie(HttpServletRequest request, @PathVariable("movieId") int movieId) {
        movieService.deleteUserMovie(request, movieId);
    }
}
