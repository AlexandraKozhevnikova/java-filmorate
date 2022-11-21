package ru.yandex.practicum.filmorate.db.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao { //todo  заменить на енам

    List<Genre> getAllGenre();

    Optional<Genre> getGenreById(int id);
}
