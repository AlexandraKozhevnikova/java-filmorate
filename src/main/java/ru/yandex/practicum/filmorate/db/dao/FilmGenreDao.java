package ru.yandex.practicum.filmorate.db.dao;

import java.util.List;

public interface FilmGenreDao {

    void upsertFilmGenres(int filmId, List<Integer> genres);

    List<Integer> getFilmGenres(int filmId);
}
