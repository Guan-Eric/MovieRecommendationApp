package com.example.MovieRecommendationBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Status")
public class Status {
    @Id
    @Column(name = "status_id")
    private Integer id;

    @Column(name = "status_name")
    private String statusName;

    public Status() {
    }

    public Status(Integer id, String statusName) {
        this.id = id;
        this.statusName = statusName;
    }

    public Integer getStatusId() {
        return id;
    }

    public void setStatusId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
