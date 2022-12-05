package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    int add(Film film);

    void upsertGenresForFilm(int filmId, List<Integer> genres);

    void update(Film film);

    List<Film> getAllItems();

    Film getItemById(int id);

    List<Integer> getFilmGenresId(int filmId);

    void likeFilm(int filmId, int userId);

    void unlikeFilm(int filmId, int userId);

    List<Film> getTopFilms(int threshold);
}

