package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.helper.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UpdateFilmRequestDto {

    private int id;
    @NotBlank(message = "'name' should non be empty")
    private String name;
    @Size(max = 200, message = "'description' should have length no more than 200")
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
