package ru.yandex.practicum.filmorate.web.dto.genre;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GenreDto {
    private final int id;
    private final String name;

    public GenreDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
