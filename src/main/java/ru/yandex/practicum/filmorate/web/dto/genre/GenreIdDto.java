package ru.yandex.practicum.filmorate.web.dto.genre;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenreIdDto {
    private int id;

    public GenreIdDto(int id) {
        this.id = id;
    }
}
