package com.example.MovieRecommendationBackend;

import com.example.MovieRecommendationBackend.service.OpenAIService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MovieRecommendationApplication {

	public static void main(String[] args) {
		// Start the Spring Boot application
		ConfigurableApplicationContext context = SpringApplication.run(MovieRecommendationApplication.class, args);

		// Get the OpenAIService bean from the application context
		OpenAIService openAIService = context.getBean(OpenAIService.class);

		// Call the generateMovieList method to test the OpenAI API interaction
		String movieList = openAIService.generateMovieList();

		// Print the generated movie list
		System.out.println("Generated movie list:");
		System.out.println(movieList);

		// Close the application context
		context.close();
	}

}
