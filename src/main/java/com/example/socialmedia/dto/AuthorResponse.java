package com.example.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
    private String message;
    private String fullName;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}