package ru.yandex.practicum.filmorate.db.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface RecommendationsDao {
    List<Film> getRecommendations(int id);
}
