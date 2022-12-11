package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    int add(Review review);

    void update(Review review);

    Optional<Review> getReviewById(int id);

    void deleteReview(int id);

    List<Review> getReviewsByFilmId(Integer filmId);

    void likeReview(int reviewId, int userId);

    void deleteLikeReview(int reviewId, int userId);

    void dislikeReview(int reviewId, int userId);

    void deleteDislikeReview(int reviewId, int userId);

    int getUsefulByReviewId(int reviewId);
}
