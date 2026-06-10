package com.example.moviespot.service;
import com.example.moviespot.dto.MovieDTO;
import com.example.moviespot.entity.Movie;
import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    Movie getMovieById(Long id);
    Movie createMovie(MovieDTO dto);
    Movie updateMovie(Long id, MovieDTO dto);
    void deleteMovie(Long id);
    List<Movie> searchMovies(String title);
    List<Movie> getMoviesByGenre(Long genreId);
}
