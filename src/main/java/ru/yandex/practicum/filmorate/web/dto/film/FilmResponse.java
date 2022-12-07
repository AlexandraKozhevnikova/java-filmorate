package ru.yandex.practicum.filmorate.web.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FilmResponse {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonProperty("mpa")
    private RatingMpa ratingMpa;
    private List<Genre> genres;
    private List<Director> directors;

    public int compareFilmsByYear(FilmResponse f2) {
        return releaseDate.isAfter(f2.getReleaseDate()) ? 1 : (releaseDate.isEqual(f2.getReleaseDate()) ? 0 : -1);
    }
}
