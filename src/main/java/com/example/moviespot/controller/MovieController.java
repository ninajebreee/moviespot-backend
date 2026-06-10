package com.example.moviespot.controller;

import com.example.moviespot.dto.MovieDTO;
import com.example.moviespot.entity.Movie;
import com.example.moviespot.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {
    @Autowired private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAll(@RequestParam(required = false) String title,
                                               @RequestParam(required = false) Long genreId) {
        if (title != null && !title.isEmpty()) return ResponseEntity.ok(movieService.searchMovies(title));
        if (genreId != null) return ResponseEntity.ok(movieService.getMoviesByGenre(genreId));
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> create(@RequestBody MovieDTO dto) {
        return ResponseEntity.ok(movieService.createMovie(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> update(@PathVariable Long id, @RequestBody MovieDTO dto) {
        return ResponseEntity.ok(movieService.updateMovie(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(Map.of("message", "Movie deleted"));
    }
}
