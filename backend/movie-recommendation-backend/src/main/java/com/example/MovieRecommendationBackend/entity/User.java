package com.example.MovieRecommendationBackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
<<<<<<< HEAD
    private Integer userId;
=======
    private Integer id;
>>>>>>> jpa-queries

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    public User() {
    }

<<<<<<< HEAD
    public User(Integer userId, String userName, String userPassword) {
        this.userId = userId;
=======
    public User(Integer id, String userName, String userPassword) {
        this.id = id;
>>>>>>> jpa-queries
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public Integer getUserId() {
<<<<<<< HEAD
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
=======
        return id;
    }

    public void setUserId(Integer id) {
        this.id = id;
>>>>>>> jpa-queries
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    // Getters and setters
}
