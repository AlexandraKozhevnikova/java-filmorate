package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class AddUserRequestDto {

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
