package ru.yandex.practicum.filmorate.web.dto.director;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DirectorResponse {
    private int id;
    private String name;
}
