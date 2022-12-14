package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class RecommendationsDaoImpl implements RecommendationsDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RecommendationsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> getRecommendations(int userId) {
        List<Integer> usersLikedFilmsIds = getUsersLikedFilmsIds(userId);

        Set<Integer> filmIdsToRecommend = new HashSet<>();
        for (Integer id : maxCommonUserIds(userId)) {
            SqlRowSet filmsRs = jdbcTemplate.queryForRowSet(
                    "SELECT film_id FROM film_like WHERE user_id = ?",
                    id
            );
            while (filmsRs.next()) {
                int foundId = filmsRs.getInt("film_id");
                if (!usersLikedFilmsIds.contains(foundId)) {
                    filmIdsToRecommend.add(foundId);
                }
            }
        }
        return List.copyOf(filmIdsToRecommend);
    }

    @Override
    public List<Integer> getCommonFilmsIds(int userId, int friendId) {
        List<Integer> userFilmIds = getUsersLikedFilmsIds(userId);
        List<Integer> otherFilmsIds = getUsersLikedFilmsIds(friendId);
        List<Integer> commonFilmsIds = new ArrayList<>();
        for (Integer id : otherFilmsIds) {
            if (userFilmIds.contains(id)) commonFilmsIds.add(id);
        }
        return commonFilmsIds;
    }

    private List<Integer> getUsersLikedFilmsIds(int user_id) {
        List<Integer> usersLikedFilmsIds = new ArrayList<>();
        SqlRowSet userRs = jdbcTemplate.queryForRowSet(
                "SELECT film_id FROM film_like WHERE user_id = ?",
                user_id
        );
        while (userRs.next()) {
            usersLikedFilmsIds.add(userRs.getInt("film_id"));
        }
        return usersLikedFilmsIds;
    }

    private List<Integer> maxCommonUserIds(int user_id) {
        List<Integer> maxCommonUserIds = new ArrayList<>();
        SqlRowSet maxCommonUserIdsRs = jdbcTemplate.queryForRowSet(
                "SELECT fl.USER_ID, count(fl.USER_ID) as common_count " +
                    "FROM film_like AS fl " +
                    "INNER JOIN film_like AS joined ON joined.FILM_ID = fl.FILM_ID " +
                        "AND joined.USER_ID = ? " +
                    "GROUP BY fl.USER_ID HAVING fl.USER_ID <> ? " +
                    "ORDER BY common_count",
                user_id, user_id
        );
        while (maxCommonUserIdsRs.next()) {
            maxCommonUserIds.add(maxCommonUserIdsRs.getInt("user_id"));
        }
        if (maxCommonUserIds.isEmpty()) {
            return Collections.emptyList();
        }
        return maxCommonUserIds;
    }
}
