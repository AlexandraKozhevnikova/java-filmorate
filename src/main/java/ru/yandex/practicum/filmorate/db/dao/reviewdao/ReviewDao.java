package ru.yandex.practicum.filmorate.db.dao.reviewdao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    int insertReview(Review review);

    void updateReview(Review review);

    Optional<Review> getReviewById(int id);

    void deleteReview(int id);

    List<Review> getReviewsByFilmId(Integer filmId);
}
