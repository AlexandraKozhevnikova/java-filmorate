package ru.yandex.practicum.filmorate.web.dto.director;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateDirectorRequest {
    private int id;
    @NotBlank(message = "'name' should non be empty")
    private String name;
}
