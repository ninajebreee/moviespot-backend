package com.example.moviespot.controller;

import com.example.moviespot.dto.ReviewDTO;
import com.example.moviespot.entity.Movie;
import com.example.moviespot.entity.Review;
import com.example.moviespot.entity.User;
import com.example.moviespot.repository.MovieRepository;
import com.example.moviespot.repository.ReviewRepository;
import com.example.moviespot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Review>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(reviewRepository.findByMovieId(movieId));
    }

    @PostMapping("/movie/{movieId}")
    public ResponseEntity<?> addReview(@PathVariable Long movieId, @RequestBody ReviewDTO dto, Authentication auth) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 10)
            return ResponseEntity.badRequest().body(Map.of("message", "Rating must be between 1 and 10"));

        Review review = new Review();
        review.setTitle(dto.getTitle());
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setMovie(movie);
        review.setUser(user);
        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewDTO dto, Authentication auth) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!review.getUser().getUsername().equals(auth.getName()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Not your review"));
        review.setTitle(dto.getTitle());
        review.setContent(dto.getContent());
        if (dto.getRating() != null) review.setRating(dto.getRating());
        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, Authentication auth) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        if (!review.getUser().getUsername().equals(auth.getName()) && !isAdmin)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Not authorized"));
        reviewRepository.delete(review);
        return ResponseEntity.ok(Map.of("message", "Review deleted"));
    }
}
