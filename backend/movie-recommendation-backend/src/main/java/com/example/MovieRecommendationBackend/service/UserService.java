package com.example.MovieRecommendationBackend.service;

import com.example.MovieRecommendationBackend.entity.User;
import com.example.MovieRecommendationBackend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> signUp(User request, HttpServletResponse response) {
        if (!userRepository.existsByUserName(request.getUserName())) {
            request.setUserPassword(hashPassword(request.getUserPassword()));

            User newUser = userRepository.save(request);

            //String hashedUserId = hashUserId(Integer.toUnsignedLong(newUser.getId()));

            Cookie cookie = new Cookie("userId", newUser.getId().toString());
            cookie.setMaxAge(60 * 60 * 24); // 1 day expiration time
            cookie.setPath("/"); // Set cookie path to root
            response.addCookie(cookie);

            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.badRequest().body("Username is already taken");
    }

    public ResponseEntity<String> login(User request, HttpServletResponse response) {
        User user = userRepository.findByUserName(request.getUserName());

        if (user.getUserPassword().equals(hashPassword(request.getUserPassword()))) {
            //String hashedUserId = hashUserId(Integer.toUnsignedLong(user.getId()));
            Cookie cookie = new Cookie("userId", user.getId().toString());
            cookie.setMaxAge(60 * 60 * 24); // 1 day expiration time
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.badRequest().body("Invalid username or password");
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }

    private String hashUserId(Long userId) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(userId.toString().getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception
            return null;
        }
    }
}
