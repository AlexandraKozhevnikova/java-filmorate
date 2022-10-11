package ru.yandex.practicum.filmorate.model;

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
    private int id;
    @NotBlank(message = "'name' should non be empty")
    private String name;
    @Size(max = 200, message = "'description' should have length no more than 200")
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
