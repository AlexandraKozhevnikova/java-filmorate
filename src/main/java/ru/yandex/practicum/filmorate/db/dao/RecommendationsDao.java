package ru.yandex.practicum.filmorate.db.dao;

import java.util.List;

public interface RecommendationsDao {
    List<Integer> getRecommendations(int id);

    List<Integer> getCommonFilmsIds(int userId, int friendId);
}
