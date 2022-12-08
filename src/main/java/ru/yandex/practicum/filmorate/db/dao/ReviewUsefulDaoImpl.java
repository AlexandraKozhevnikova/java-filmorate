package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadReviewReactionException;

@Component
@Slf4j
public class ReviewUsefulDaoImpl implements ReviewUsefulDao {

    private final JdbcTemplate jdbcTemplate;

    public ReviewUsefulDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void likeReview(int reviewId, int userId) {
        String sql = "INSERT INTO review_useful (review_id, user_id, useful) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, userId, 1);
    }

    @Override
    public void deleteLikeReview(int reviewId, int userId) {
        checkReactionType(reviewId, userId);
        String sql = "DELETE FROM review_useful WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, reviewId, userId);
    }

    @Override
    public void dislikeReview(int reviewId, int userId) {
        String sql = "INSERT INTO review_useful (review_id, user_id, useful) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, userId, -1);
    }

    @Override
    public void deleteDislikeReview(int reviewId, int userId) {
        checkReactionType(reviewId, userId);
        String sql = "DELETE FROM review_useful WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, reviewId, userId);
    }

    @Override
    public Integer getUsefulByReviewId(int reviewId) {
        String sql = "SELECT SUM(useful) FROM review_useful WHERE review_id = ?";
        Integer useful = jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
        if (useful == null)
            return 0;
        return useful;
    }

    /**
     * Метод определяет тип реакции: лайк или дизлайк. Если передаёт положительное число (должно быть +1),
     * значит это лайк, если отрицательное (должно быть -1), значит дизлайк.
     */

    private void checkReactionType(int reviewId, int userId) {
        String sql = "SELECT useful FROM review_useful WHERE review_id = ? AND user_id = ?";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, reviewId, userId);
        } catch (DataAccessException e) {
            throw new BadReviewReactionException("Field 'useful' for review with id = " + reviewId +
                    " or for user with id = " + userId + " doesn't exist in db.");
        }
    }
}
