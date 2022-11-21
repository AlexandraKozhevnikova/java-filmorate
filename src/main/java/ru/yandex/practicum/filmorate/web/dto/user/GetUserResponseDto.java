package ru.yandex.practicum.filmorate.web.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetUserResponseDto {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
