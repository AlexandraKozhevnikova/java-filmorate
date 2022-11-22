package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    int add(Film film);

    void upsertGenresForFilm(int filmId, List<Integer> genres);

    void update(Film film);

    List<Film> getAllItems();

    Optional<Film> getItemById(int id);

    List<Integer> getFilmGenresId(int filmId);
}
