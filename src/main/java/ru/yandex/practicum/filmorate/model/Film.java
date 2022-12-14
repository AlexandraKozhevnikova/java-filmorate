package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.web.dto.film.FilmResponse;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Film implements IdControl {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int ratingMpaId;
    private List<Integer> genres;
    private List<Director> director;

    public void setGenres(List<Integer> genres) {
        this.genres = Objects.requireNonNullElse(genres, Collections.emptyList());
    }

    public void setDirectors(List<Director> director) {
        this.director = Objects.requireNonNullElse(director, Collections.emptyList());
    }

    public int compareFilmsByYear(Film f2) {
        return releaseDate.isAfter(f2.getReleaseDate()) ? 1 : (releaseDate.isEqual(f2.getReleaseDate()) ? 0 : -1);
    }
}
