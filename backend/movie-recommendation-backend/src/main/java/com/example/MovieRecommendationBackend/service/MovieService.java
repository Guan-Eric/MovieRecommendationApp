package com.example.MovieRecommendationBackend.service;

import com.example.MovieRecommendationBackend.entity.Movie;
import com.example.MovieRecommendationBackend.entity.Status;
import com.example.MovieRecommendationBackend.entity.UserMovie;
import com.example.MovieRecommendationBackend.entity.UserMovieId;
import com.example.MovieRecommendationBackend.repository.UserMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.MovieRecommendationBackend.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    UserMovieRepository userMovieRepository;

    public List<UserMovie> getUserMoviesByStatusId(int userId, int statusId) {
        return userMovieRepository.findByUserIdAndStatusId(userId, statusId);
    }

    public UserMovie updateUserMovieStatus(UserMovie userMovie, int id) {
        Status status = new Status();
        status.setStatusId(id);
        userMovie.setStatus(status);
        return userMovieRepository.save(userMovie);
    }

    public UserMovie getUserMovie(int userId, int movieId) {
        return userMovieRepository.findById(new UserMovieId(userId, movieId)).get();
    }

    public UserMovie saveUserMovie(UserMovie userMovie) {
        return userMovieRepository.save(userMovie);
    }

    public void deleteUserMovie(int userId, int movieId) {
        userMovieRepository.deleteById(new UserMovieId(userId, movieId));
    }
}
