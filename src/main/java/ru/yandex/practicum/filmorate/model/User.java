package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User implements IdControl {

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    public Set<Integer> friends = new HashSet<>();

    public User() {
    }

    public String getName() {
        return name.isBlank() ? login : name;
    }
}
