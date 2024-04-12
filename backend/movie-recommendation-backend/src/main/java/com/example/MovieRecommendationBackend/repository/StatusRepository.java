package com.example.MovieRecommendationBackend.repository;

import com.example.MovieRecommendationBackend.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
}
