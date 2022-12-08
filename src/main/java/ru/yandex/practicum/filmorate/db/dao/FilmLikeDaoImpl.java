package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FilmLikeDaoImpl implements FilmLikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void likeFilm(int filmId, int userId) throws DuplicateKeyException {
        String sql = "INSERT INTO film_like (film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        String sql = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Map<String, Object>> getTopLikes(int threshold, Integer genreId, String year) {
        if (year == null) {
            year = "0000";
        }

        if (genreId == null) {
            genreId = -1;
        }

        String sql = "WITH " +
                "genre_filtred(film_id) AS (" +
                "   SELECT film_id " +
                "   FROM film_genre " +
                "   WHERE 1 = 1 " +
                (genreId == -1 ? " AND 1 <> ?" : " AND genre_id = ?") +
                "), " +
                "film_filtred(film_id) AS (" +
                "   SELECT id " +
                "   FROM film " +
                "   WHERE 1=1 " +
                (year == "0000" ? " AND 1 <> ? " : "AND  EXTRACT(YEAR FROM release_date) = ?") +
                ") " +
                "SELECT film_like.film_id, COUNT(user_id) as count_likes " +
                "FROM film_like " +
                (genreId == -1 ? " LEFT " : " ") +
                "    JOIN genre_filtred ON FILM_LIKE.FILM_ID = genre_filtred.film_id " +
                (year == "0000" ? " LEFT " : " ") +
                "    JOIN film_filtred ON FILM_LIKE.FILM_ID = film_filtred.film_id " +
                "GROUP BY FILM_LIKE.film_id " +
                "ORDER BY count_likes desc " +
                "LIMIT ? ";

        return jdbcTemplate.queryForList(sql, genreId, year, threshold);
    }
}
