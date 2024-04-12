package com.example.MovieRecommendationBackend.repository;

import com.example.MovieRecommendationBackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
