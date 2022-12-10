package ru.yandex.practicum.filmorate.db.dao;

public interface ReviewUsefulDao {
    void likeReview(int reviewId, int userId);

    void deleteLikeReview(int reviewId, int userId);

    void dislikeReview(int reviewId, int userId);

    void deleteDislikeReview(int reviewId, int userId);

    int getUsefulByReviewId(int reviewId);
}
