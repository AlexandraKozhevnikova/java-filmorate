package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.data.relational.core.sql.In;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreDao {

    void upsertFilmGenres(int filmId, List<Integer> genres);

    List<Integer> getFilmGenres(int filmId);
}
