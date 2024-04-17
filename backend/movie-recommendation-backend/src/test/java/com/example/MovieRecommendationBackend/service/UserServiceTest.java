package com.example.MovieRecommendationBackend.service;

import com.example.MovieRecommendationBackend.entity.User;
import com.example.MovieRecommendationBackend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSignUp_Success() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setUserName("testUser");
        newUser.setUserPassword("testPassword");

        when(userRepository.existsByUserName("testUser")).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(newUser);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ResponseEntity<String> responseEntity = userService.signUp(newUser, response);

        assertEquals(ResponseEntity.ok("User registered successfully"), responseEntity);

        verify(userRepository, times(1)).existsByUserName("testUser");
        verify(userRepository, times(1)).save(newUser);
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void testSignUp_Failure_UsernameTaken() {
        User existingUser = new User();
        existingUser.setUserName("existingUser");
        existingUser.setUserPassword("password");

        when(userRepository.existsByUserName("existingUser")).thenReturn(true);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ResponseEntity<String> responseEntity = userService.signUp(existingUser, response);

        assertEquals(ResponseEntity.badRequest().body("Username is already taken"), responseEntity);

        verify(userRepository, never()).save(any(User.class));
        verify(response, never()).addCookie(any(Cookie.class));
    }

    @Test
    void testLogin_Success() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUserName("testUser");
        existingUser.setUserPassword(UserService.hashPassword("testPassword")); // Hashed password

        when(userRepository.findByUserName("testUser")).thenReturn(existingUser);

        User loginRequest = new User();
        loginRequest.setUserName("testUser");
        loginRequest.setUserPassword("testPassword");

        HttpServletResponse response = mock(HttpServletResponse.class);
        ResponseEntity<String> responseEntity = userService.login(loginRequest, response);

        assertEquals(ResponseEntity.ok("Login successful"), responseEntity);

        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void testLogin_Failure_InvalidUsernameOrPassword() {
        when(userRepository.findByUserName("nonExistentUser")).thenReturn(null);

        User loginRequest = new User();
        loginRequest.setUserName("nonExistentUser");
        loginRequest.setUserPassword("testPassword");

        HttpServletResponse response = mock(HttpServletResponse.class);
        ResponseEntity<String> responseEntity = userService.login(loginRequest, response);

        assertEquals(ResponseEntity.badRequest().body("Invalid username or password"), responseEntity);

        verify(response, never()).addCookie(any(Cookie.class));
    }
}