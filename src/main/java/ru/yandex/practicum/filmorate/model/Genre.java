package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;

import java.util.List;
import java.util.Optional;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genre {
    COMEDY("Комедия", 1),
    DRAMA("Драма", 2),
    CARTOON("Мультфильм", 3),
    THRILLER("Триллер", 4),
    DOCUMENTARY("Документальный", 5),
    ACTION_MOVIE("Экшен", 6);

    private final int id;
    private final String name;

    Genre(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Optional<Genre> getGenreById(int id) {
        for (Genre genre : Genre.values()) {
            if (genre.getId() == id) {
                return Optional.of(genre);
            }
        }
        return Optional.empty();
    }
}
