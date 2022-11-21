package ru.yandex.practicum.filmorate.web.dto.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.web.dto.RatingMpaId;
import ru.yandex.practicum.filmorate.web.dto.genre.GenreIdDto;
import ru.yandex.practicum.filmorate.web.validation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilmRequestDto {

    private int id;
    @NotBlank(message = "'name' should non be empty")
    private String name;
    @Size(max = 200, message = "'description' should have length no more than 200")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @JsonProperty("mpa")
    private RatingMpaId ratingMpaId;
    private List<GenreIdDto> genres;
}
