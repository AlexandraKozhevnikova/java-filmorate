package ru.yandex.practicum.filmorate.db.dao;

import java.util.List;
import java.util.Map;

public interface FilmLikeDao {

    void likeFilm(int filmId, int userId);

    void unlikeFilm(int filmId, int userId);

    List<Map<String, Object>> getTopLikes(int threshold);
}
