package com.example.socialmedia.repository;

import com.example.socialmedia.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCreatedById(Long authorId);

    List<Post> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    void deleteByCreatedById(Long authorId);
}