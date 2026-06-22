package com.example.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String message;
    private String title;
    private String content;
    private String visibility;
    private LocalDateTime createdAt;
    private String createdBy;
}