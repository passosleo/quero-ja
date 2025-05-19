package com.leonardo.queroja.entities;

import com.leonardo.queroja.enums.Priority;

import java.time.LocalDateTime;

public class WishEntity {

    private Integer wishId;
    private Integer userId;
    private String title;
    private String description;
    private String link;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WishEntity(Integer wishId, Integer userId, String title, String description, String link, Priority priority) {
        this.wishId = wishId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getWishId() {
        return wishId;
    }

    public void setWishId(Integer wishId) {
        this.wishId = wishId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
