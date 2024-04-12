package com.example.MovieRecommendationBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserMovieId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "movie_id")
    private Integer movieId;

    public UserMovieId() {
    }

    public UserMovieId(Integer userId, Integer movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

   }
