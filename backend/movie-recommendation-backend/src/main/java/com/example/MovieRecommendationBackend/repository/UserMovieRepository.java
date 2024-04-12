package com.example.MovieRecommendationBackend.repository;
import com.example.MovieRecommendationBackend.entity.Movie;
import com.example.MovieRecommendationBackend.entity.UserMovie;
import com.example.MovieRecommendationBackend.entity.UserMovieId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserMovieRepository extends CrudRepository<UserMovie, UserMovieId> {

    List<UserMovie> findByStatus_StatusId(Integer statusId);
}

