package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Component
public class RecommendationsDaoImpl implements RecommendationsDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;


    @Autowired
    public RecommendationsDaoImpl(JdbcTemplate jdbcTemplate, @Qualifier("dbFilmStorage") FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }

    @Override
    public List<Film> getRecommendations(int user_id) {
        List<Film> films = new ArrayList<>();
        List<Integer> usersLikedFilmsIds = getUsersLikedFilmsIds(user_id);

        Set<Integer> filmIdsToRecommend = new HashSet<>();
        for (Integer userId : maxCommonUserIds(user_id)) {
            SqlRowSet filmsRs = jdbcTemplate.queryForRowSet(
                    "SELECT film_id from film_like WHERE user_id = ?",
                    userId
            );
            while (filmsRs.next()) {
                int foundId = filmsRs.getInt("film_id");
                if (!usersLikedFilmsIds.contains(foundId)) filmIdsToRecommend.add(foundId);
            }
        }

        for (Integer id : filmIdsToRecommend) {
            Optional<Film> film = filmStorage.getItemById(id);
            film.ifPresent(films::add);
        }
        return films;
    }

    private List<Integer> getUsersLikedFilmsIds(int user_id) {
        List<Integer> usersLikedFilmsIds = new ArrayList<>();
        SqlRowSet userRs = jdbcTemplate.queryForRowSet(
                "SELECT film_id from film_like WHERE user_id = ?",
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
                "SELECT USER_ID, MAX(common_count) as max_common FROM (SELECT USER_ID, COUNT(USER_ID) as common_count FROM FILM_LIKE WHERE FILM_ID IN (SELECT FILM_ID FROM film_like WHERE user_id = ?) AND USER_ID <> ? GROUP BY user_id) group by common_count",
                user_id, user_id
        );
        while (maxCommonUserIdsRs.next()) {
            maxCommonUserIds.add(maxCommonUserIdsRs.getInt("USER_ID"));
        }
        if (maxCommonUserIds.isEmpty()) return Collections.emptyList();
        return maxCommonUserIds;
    }
}
