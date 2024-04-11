package com.example.MovieRecommendationBackend.repository;

import com.example.MovieRecommendationBackend.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {
}
