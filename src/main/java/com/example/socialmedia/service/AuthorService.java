package com.example.socialmedia.service;

import com.example.socialmedia.dto.CreateAuthorRequest;
import com.example.socialmedia.dto.AuthorResponse;
import com.example.socialmedia.model.Author;
import com.example.socialmedia.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorResponse createAuthor(CreateAuthorRequest request) {

        if (request.getFullName() == null || request.getFullName().length() < 5) {
            return new AuthorResponse("Full name must contain at least 5 characters",
                    null, null, null, null);
        }

        if (authorRepository.existsByUsername(request.getUsername())) {
            return new AuthorResponse("Username already exists", null, null, null, null);
        }

        Optional<Author> existingEmail = authorRepository.findByEmail(request.getEmail());
        if (existingEmail.isPresent()) {
            return new AuthorResponse("Email already exists", null, null, null, null);
        }

        Author author = new Author();
        author.setFullName(request.getFullName());
        author.setUsername(request.getUsername());
        author.setEmail(request.getEmail());
        author.setCreatedAt(LocalDateTime.now());

        Author savedAuthor = authorRepository.save(author);

        return new AuthorResponse(
                "Author created successfully",
                savedAuthor.getFullName(),
                savedAuthor.getUsername(),
                savedAuthor.getEmail(),
                savedAuthor.getCreatedAt()
        );
    }

    public Author getAuthorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElse(null);
    }

    public Author getAuthorByUsername(String username) {
        Optional<Author> author = authorRepository.findByUsername(username);
        return author.orElse(null);
    }

    public Author getAuthorByEmail(String email) {
        Optional<Author> author = authorRepository.findByEmail(email);
        return author.orElse(null);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> getAuthorsByDateRange(LocalDateTime start, LocalDateTime end) {
        return authorRepository.findByCreatedAtBetween(start, end);
    }

    public Author updateAuthorBio(Long id, String bio) {
        Author author = getAuthorById(id);
        if (author != null) {
            author.setBio(bio);
            return authorRepository.save(author);
        }
        return null;
    }

    public Author updateAuthorProfile(Long id, CreateAuthorRequest request) {
        Author author = getAuthorById(id);
        if (author == null) {
            return null;
        }

        if (request.getFullName().length() < 5) {
            return null;
        }

        if (!author.getUsername().equals(request.getUsername()) &&
                authorRepository.existsByUsername(request.getUsername())) {
            return null;
        }

        author.setFullName(request.getFullName());
        author.setUsername(request.getUsername());
        author.setEmail(request.getEmail());

        return authorRepository.save(author);
    }

    public boolean deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        if (author != null) {
            authorRepository.delete(author);
            return true;
        }
        return false;
    }
}