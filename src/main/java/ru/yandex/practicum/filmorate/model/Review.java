package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class Review {
    private int id;
    private String content;
    private boolean isPositive;
    @NotNull(message = "'userId' must not be null")
    private int userId;
    @NotNull(message = "'filmId' must not be null")
    private int filmId;
    private int useful;

    public boolean getIsPositive() {
        return isPositive;
    }
}
