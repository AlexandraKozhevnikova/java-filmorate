package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User implements IdControl {

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    public Set<Integer> friends = new HashSet<>();

    public String getName() {
        return name == null ? login : name;
    }
}
