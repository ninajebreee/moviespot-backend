package com.example.moviespot.controller;

import com.example.moviespot.entity.Genre;
import com.example.moviespot.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/genres")
@CrossOrigin(origins = "*")
public class GenreController {
    @Autowired private GenreRepository genreRepository;

    @GetMapping public ResponseEntity<List<Genre>> getAll() { return ResponseEntity.ok(genreRepository.findAll()); }

    @GetMapping("/{id}") public ResponseEntity<Genre> getById(@PathVariable Long id) {
        return ResponseEntity.ok(genreRepository.findById(id).orElseThrow());
    }

    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> create(@RequestBody Genre genre) { return ResponseEntity.ok(genreRepository.save(genre)); }

    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> update(@PathVariable Long id, @RequestBody Genre updated) {
        Genre g = genreRepository.findById(id).orElseThrow();
        g.setName(updated.getName()); g.setDescription(updated.getDescription());
        return ResponseEntity.ok(genreRepository.save(g));
    }

    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        genreRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Genre deleted"));
    }
}
