package com.example.MovieRecommendationBackend.service;

import com.example.MovieRecommendationBackend.entity.User;
import com.example.MovieRecommendationBackend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> signUp(User request, HttpServletResponse response) {
        if (!userRepository.existsByUserName(request.getUserName())) {
            request.setUserPassword(hashPassword(request.getUserPassword()));

            User newUser = userRepository.save(request);

            //String hashedUserId = hashUserId(Integer.toUnsignedLong(newUser.getId()));

            Cookie cookie = new Cookie("userId", newUser.getId().toString());
            cookie.setMaxAge(60 * 60 * 24); // 1 day expiration time
            cookie.setPath("/"); // Set cookie path to root
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("message","User registered successfully"));

        }
        return ResponseEntity.badRequest().body(Map.of("error", "Username is already taken"));
    }

    public ResponseEntity<?> login(@RequestBody User request, HttpServletResponse response) {
        User user = userRepository.findByUserName(request.getUserName());

        if (request.getUserName() == null || request.getUserName().isEmpty() ||
                request.getUserPassword() == null || request.getUserPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username or password cannot be empty"));
        }

        if (user != null && user.getUserPassword().equals(hashPassword(request.getUserPassword()))) {
            //String hashedUserId = hashUserId(Integer.toUnsignedLong(user.getId()));
            Cookie cookie = new Cookie("userId", user.getId().toString());
            cookie.setMaxAge(60 * 60 * 24); // 1 day expiration time
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
    }

    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return ResponseEntity.ok("Logout successful");
                }
            }
        }
        return ResponseEntity.badRequest().body("Logout failed");
    }

    public ResponseEntity<?> checkUserAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        boolean isAuthenticated = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty() &&
                        userRepository.existsById(Integer.parseInt(cookie.getValue()))) { // Validate user exists
                    isAuthenticated = true;
                    break;
                }
            }
        }
        return isAuthenticated ?
                ResponseEntity.ok(Map.of("isAuthenticated", true)) :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("isAuthenticated", false));
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
