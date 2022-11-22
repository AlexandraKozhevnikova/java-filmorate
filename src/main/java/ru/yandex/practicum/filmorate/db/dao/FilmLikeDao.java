package ru.yandex.practicum.filmorate.db.dao;

public interface FilmLikeDao {

    void likeFilm(int filmId, int userId);
}
