package com.example.MovieRecommendationBackend.controller;

import com.example.MovieRecommendationBackend.entity.Movie;
import com.example.MovieRecommendationBackend.entity.MovieInput;
import com.example.MovieRecommendationBackend.entity.User;
import com.example.MovieRecommendationBackend.entity.UserMovie;
import com.example.MovieRecommendationBackend.service.MovieService;
import com.example.MovieRecommendationBackend.service.UserService;
import com.google.gson.JsonObject;
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
    public ResponseEntity<String> addUserMovie(HttpServletRequest request, @RequestBody MovieInput movieInput) {
        return movieService.saveUserMovie(request, movieInput);
    }

    @PutMapping("/editmovie")
    public ResponseEntity<String> updateUserMovieStatus(HttpServletRequest request, @RequestBody MovieInput movieInput) {
        return movieService.updateUserMovieStatus(request, movieInput);
    }

    @DeleteMapping("/removemovie")
    public void deleteUserMovie(HttpServletRequest request, @RequestBody MovieInput movieInput) {
        movieService.deleteUserMovie(request, movieInput);
    }

    @PostMapping("/generate")
    public List<Movie> generateUserMovies(HttpServletRequest request, @RequestBody List<String> constraints) {
        return movieService.generateRecommendations(request, constraints);
    }
}
