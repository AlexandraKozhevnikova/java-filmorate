package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Film implements IdControl {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonProperty("mpa")
    private RatingMPA ratingMPA;
    private List<Integer> genres;
}
