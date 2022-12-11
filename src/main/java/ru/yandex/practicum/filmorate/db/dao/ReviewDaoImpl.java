package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertReview(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("review")
                .usingGeneratedKeyColumns("review_id");
        return simpleJdbcInsert.executeAndReturnKey(mapReviewToMap(review)).intValue();
    }

    @Override
    public void updateReview(Review review) {
        String sql = "UPDATE review SET content = ?, isPositive = ? " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getId()
        );
    }

    @Override
    public Optional<Review> getReviewById(int id) {
        String sql = "SELECT review_id, content, isPositive, user_id, film_id FROM review WHERE review_id = ?";
        Optional<Review> review = Optional.empty();
        try {
            review = Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToReview, id));
        } catch (EmptyResultDataAccessException ignored) {
        }
        return review;
    }

    @Override
    public void deleteReview(int id) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Review> getReviewsByFilmId(Integer filmId) {
        String sql;
        if (filmId == null) {
            sql = "SELECT * FROM review";
            return jdbcTemplate.query(sql, this::mapRowToReview);
        } else {
            sql = "SELECT * FROM review WHERE film_id = ?";
            return jdbcTemplate.query(sql, this::mapRowToReview, filmId);
        }
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .id(resultSet.getInt("review_id"))
                .content(resultSet.getString("content"))
                .isPositive(resultSet.getBoolean("isPositive"))
                .userId(resultSet.getInt("user_id"))
                .filmId(resultSet.getInt("film_id"))
                .build();
    }

    private Map<String, Object> mapReviewToMap(Review review) {
        Map<String, Object> map = new HashMap<>();
        map.put("review_id", review.getId());
        map.put("content", review.getContent());
        map.put("isPositive", review.getIsPositive());
        map.put("user_id", review.getUserId());
        map.put("film_id", review.getFilmId());

        return map;
    }
}
