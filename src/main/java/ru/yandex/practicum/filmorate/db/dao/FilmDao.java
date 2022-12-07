package ru.yandex.practicum.filmorate.db.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    int insertFilm(Film film);

    boolean isExist(int id);

    void update(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilmById(int id);

    List<Integer> getFilteredFilm(int count, List<Integer> excludeList, Integer genreId, String year);
}

