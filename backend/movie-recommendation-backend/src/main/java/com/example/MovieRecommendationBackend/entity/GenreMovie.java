package com.example.MovieRecommendationBackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GenreMovie")
public class GenreMovie {
    @Id
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Id
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public GenreMovie() {
    }

    public GenreMovie(Genre genre, Movie movie) {
        this.genre = genre;
        this.movie = movie;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
