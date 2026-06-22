package com.example.socialmedia.service;

import com.example.socialmedia.dto.CreatePostRequest;
import com.example.socialmedia.dto.PostResponse;
import com.example.socialmedia.model.Author;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthorService authorService;

    public PostResponse createPost(CreatePostRequest request) {

        if (request.getTitle() == null || request.getTitle().length() < 10 ||
                request.getTitle().length() > 100) {
            return new PostResponse("Title must be between 10 and 100 characters",
                    null, null, null, null, null);
        }


        if (request.getContent() == null || request.getContent().length() < 50 ||
                request.getContent().length() > 2000) {
            return new PostResponse("Content must be between 50 and 2000 characters",
                    null, null, null, null, null);
        }

        Author author = authorService.getAuthorById(request.getAuthorId());
        if (author == null) {
            return new PostResponse("Author not found", null, null, null, null, null);
        }

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCreatedBy(author);
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return new PostResponse(
                "Post created successfully",
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getVisibility(),
                savedPost.getCreatedAt(),
                savedPost.getCreatedBy().getFullName()
        );
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByAuthor(Long authorId) {
        Author author = authorService.getAuthorById(authorId);
        if (author == null) {
            return List.of();
        }
        return postRepository.findByCreatedById(authorId);
    }

    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null);
    }

    public Post updatePost(Long id, CreatePostRequest request) {
        Post post = getPostById(id);
        if (post == null) {
            return null;
        }


        if (request.getTitle().length() < 10 || request.getTitle().length() > 100) {
            return null;
        }

        if (request.getContent().length() < 50 || request.getContent().length() > 2000) {
            return null;
        }

        if (!post.getCreatedBy().getId().equals(request.getAuthorId())) {
            return null;
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return postRepository.save(post);
    }

    public boolean deletePost(Long id) {
        Post post = getPostById(id);
        if (post != null) {
            postRepository.delete(post);
            return true;
        }
        return false;
    }

    public boolean deletePostsByAuthor(Long authorId) {
        Author author = authorService.getAuthorById(authorId);
        if (author == null) {
            return false;
        }
        postRepository.deleteByCreatedById(authorId);
        return true;
    }

    public boolean deletePostsByDateRange(LocalDateTime start, LocalDateTime end) {
        List<Post> posts = postRepository.findByCreatedAtBetween(start, end);
        if (!posts.isEmpty()) {
            postRepository.deleteAll(posts);
            return true;
        }
        return false;
    }
}