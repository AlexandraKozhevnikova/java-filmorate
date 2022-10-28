package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddUserResponseDto {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
