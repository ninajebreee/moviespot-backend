package com.example.moviespot.dto;
import java.util.Set;
public class MovieDTO {
    private Long id; private String title; private String description;
    private String director; private Integer releaseYear; private Integer duration;
    private String posterUrl; private String trailerUrl; private Set<Long> genreIds;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; } public void setTitle(String t) { this.title = t; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
    public String getDirector() { return director; } public void setDirector(String d) { this.director = d; }
    public Integer getReleaseYear() { return releaseYear; } public void setReleaseYear(Integer y) { this.releaseYear = y; }
    public Integer getDuration() { return duration; } public void setDuration(Integer d) { this.duration = d; }
    public String getPosterUrl() { return posterUrl; } public void setPosterUrl(String u) { this.posterUrl = u; }
    public String getTrailerUrl() { return trailerUrl; } public void setTrailerUrl(String u) { this.trailerUrl = u; }
    public Set<Long> getGenreIds() { return genreIds; } public void setGenreIds(Set<Long> g) { this.genreIds = g; }
}
