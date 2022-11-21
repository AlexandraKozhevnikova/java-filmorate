package ru.yandex.practicum.filmorate.web.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.web.dto.RatingMpaId;
import ru.yandex.practicum.filmorate.web.dto.genre.GenreIdDto;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UpdateFilmResponseDto {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonProperty("mpa")
    private RatingMpaId ratingMpaId;
    private List<GenreIdDto> genres;
}

