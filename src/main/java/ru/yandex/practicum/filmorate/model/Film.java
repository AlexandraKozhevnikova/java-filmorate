package ru.yandex.practicum.filmorate.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.helper.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Film implements IdControl {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;
    @NotBlank(message = "'name' should non be empty")
    private String name;
    @Size(max = 200, message = "'description' should have length no more than 200")
    private String description;

    @Schema(description = "Дата выхода. Старше 28 декабря 1895 года")
    @ReleaseDate
    private LocalDate releaseDate;
    @Schema(
            description = "Продолжительность (в секундах). Больше нуля",
    example = "9000")
    @Positive
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
