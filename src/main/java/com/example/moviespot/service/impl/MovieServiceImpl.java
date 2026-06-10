package com.example.moviespot.service.impl;

import com.example.moviespot.dto.MovieDTO;
import com.example.moviespot.entity.Genre;
import com.example.moviespot.entity.Movie;
import com.example.moviespot.repository.GenreRepository;
import com.example.moviespot.repository.MovieRepository;
import com.example.moviespot.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {
    @Autowired private MovieRepository movieRepository;
    @Autowired private GenreRepository genreRepository;

    @Override public List<Movie> getAllMovies() { return movieRepository.findAll(); }

    @Override public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found: " + id));
    }

    @Override public Movie createMovie(MovieDTO dto) {
        Movie movie = new Movie();
        mapDto(dto, movie);
        return movieRepository.save(movie);
    }

    @Override public Movie updateMovie(Long id, MovieDTO dto) {
        Movie movie = getMovieById(id);
        mapDto(dto, movie);
        return movieRepository.save(movie);
    }

    @Override public void deleteMovie(Long id) {
        movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        movieRepository.deleteById(id);
    }

    @Override public List<Movie> searchMovies(String title) { return movieRepository.findByTitleContainingIgnoreCase(title); }
    @Override public List<Movie> getMoviesByGenre(Long genreId) { return movieRepository.findByGenresId(genreId); }

    private void mapDto(MovieDTO dto, Movie movie) {
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDirector(dto.getDirector());
        movie.setReleaseYear(dto.getReleaseYear());
        movie.setDuration(dto.getDuration());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setTrailerUrl(dto.getTrailerUrl());
        if (dto.getGenreIds() != null) {
            Set<Genre> genres = new HashSet<>();
            dto.getGenreIds().forEach(gId -> genreRepository.findById(gId).ifPresent(genres::add));
            movie.setGenres(genres);
        }
    }
}
