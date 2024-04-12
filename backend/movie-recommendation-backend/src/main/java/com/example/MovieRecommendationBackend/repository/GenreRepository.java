package com.example.MovieRecommendationBackend.repository;

import com.example.MovieRecommendationBackend.entity.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer> {
}
