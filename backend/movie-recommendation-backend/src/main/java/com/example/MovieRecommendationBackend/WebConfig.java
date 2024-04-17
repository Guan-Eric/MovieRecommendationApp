package com.example.MovieRecommendationBackend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // You can restrict paths with specific patterns here.
                .allowedOrigins("http://localhost:3000") // Specify the allowed origins.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify the allowed methods.
                .allowedHeaders("*") // Use specific headers or use '*' for all headers.
                .allowCredentials(true); // If you want to include cookies in your requests.
    }
}

