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
    public List<Map<String, Object>> getTopLikes(int threshold) {
        String sql = "SELECT film_id, count(user_id) as count_likes " +
                "FROM film_like " +
                "GROUP by film_id " +
                "ORDER by count_likes desc " +
                "LIMIT ?";

        return jdbcTemplate.queryForList(sql, threshold);
    }


}
