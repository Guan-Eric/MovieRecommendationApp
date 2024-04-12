package com.example.MovieRecommendationBackend.repository;

import com.example.MovieRecommendationBackend.entity.GenreMovie;
import com.example.MovieRecommendationBackend.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreMovieRepository extends CrudRepository<GenreMovie, Integer> {
    List<Movie> findMoviesByGenreId(Integer genreId);
}
