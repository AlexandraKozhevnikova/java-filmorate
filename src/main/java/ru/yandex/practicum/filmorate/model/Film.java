package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

    public void setGenres(List<Integer> genres) {
        this.genres = Objects.requireNonNullElse(genres, Collections.emptyList());
    }
}
