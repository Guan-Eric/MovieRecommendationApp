package com.example.MovieRecommendationBackend.controller;

import com.example.MovieRecommendationBackend.entity.UserMovie;
import com.example.MovieRecommendationBackend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/movie")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/movies/{userId}/{statusId}")
    public List<UserMovie> getAllByStatusId(@PathVariable("userId") int userId, @PathVariable("statusId") int statusId) {
        return movieService.getUserMoviesByStatusId(userId, statusId);
    }

    @PostMapping("/add")
    public UserMovie addUserMovie(@RequestBody UserMovie userMovie) {
        return movieService.saveUserMovie(userMovie);
    }

    @GetMapping("/get/{userId}/{movieId}")
    public UserMovie getUserMovie(@PathVariable("userId") int userId, @PathVariable("movieId") int movieId) {
        return movieService.getUserMovie(userId, movieId);
    }

    @PutMapping("/status/{id}")
    public UserMovie updateUserMovieStatus(@RequestBody UserMovie userMovie, @PathVariable("id") int id) {
        return movieService.updateUserMovieStatus(userMovie, id);
    }

    @DeleteMapping("/delete/{userId}/{movieId}")
    public void deleteUserMovie(@PathVariable("userId") int userId, @PathVariable("movieId") int movieId) {
        movieService.deleteUserMovie(userId, movieId);
    }
}
