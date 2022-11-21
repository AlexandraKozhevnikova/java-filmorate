package ru.yandex.practicum.filmorate.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RatingMpaId {
    private int id;

    public RatingMpaId(int id) {
        this.id = id;
    }
}
