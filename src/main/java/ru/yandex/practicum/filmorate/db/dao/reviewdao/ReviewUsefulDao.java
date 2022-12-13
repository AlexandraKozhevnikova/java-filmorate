package ru.yandex.practicum.filmorate.db.dao.reviewdao;

public interface ReviewUsefulDao {
    void likeReview(int reviewId, int userId);

    void deleteLikeReview(int reviewId, int userId);

    void dislikeReview(int reviewId, int userId);

    void deleteDislikeReview(int reviewId, int userId);

    Integer getUsefulByReviewId(int reviewId);
}
