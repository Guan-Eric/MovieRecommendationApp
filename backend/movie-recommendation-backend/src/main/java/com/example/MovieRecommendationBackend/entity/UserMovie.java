package com.example.MovieRecommendationBackend.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "UserMovie")
public class UserMovie {

    @EmbeddedId
    public UserMovieId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "rating")
    private Integer rating;

    public UserMovie() {
    }

    public UserMovie(User user, Movie movie, Status status, Integer rating) {
        this.user = user;
        this.movie = movie;
        this.status = status;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}

