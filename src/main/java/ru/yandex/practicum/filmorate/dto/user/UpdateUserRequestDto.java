package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UpdateUserRequestDto {

    private int id;
    @Email(message = "'email' should not be blank")
    private String email;
    @NotBlank(message = "'login' should not be blank")
    private String login;
    private String name;
    @PastOrPresent(message = "'birthday' should not be from the future")
    private LocalDate birthday;

    public String getName() {
        return name == null ? login : name;
    }
}
