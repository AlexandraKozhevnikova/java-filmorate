package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.ReviewDao;
import ru.yandex.practicum.filmorate.db.dao.ReviewUsefulDao;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("dbReviewStorage")
public class DbReviewStorage implements ReviewStorage {
    private final ReviewDao reviewDao;
    private final ReviewUsefulDao reviewUsefulDao;

    @Autowired
    public DbReviewStorage(ReviewDao reviewDao, ReviewUsefulDao reviewUsefulDao) {
        this.reviewDao = reviewDao;
        this.reviewUsefulDao = reviewUsefulDao;
    }

    @Override
    public int add(Review review) {
        return reviewDao.insertReview(review);
    }

    @Override
    public void update(Review review) {
        reviewDao.updateReview(review);
    }

    @Override
    public Optional<Review> getReviewById(int id) {
        return reviewDao.getReviewById(id);
    }

    @Override
    public void deleteReview(int id) {
        reviewDao.deleteReview(id);
    }

    @Override
    public List<Review> getReviewsByFilmId(Integer filmId) {
        return reviewDao.getReviewsByFilmId(filmId);
    }

    @Override
    public void likeReview(int reviewId, int userId) {
        reviewUsefulDao.likeReview(reviewId, userId);
    }

    @Override
    public void deleteLikeReview(int reviewId, int userId) {
        reviewUsefulDao.deleteLikeReview(reviewId, userId);
    }

    @Override
    public void dislikeReview(int reviewId, int userId) {
        reviewUsefulDao.dislikeReview(reviewId, userId);
    }

    @Override
    public void deleteDislikeReview(int reviewId, int userId) {
        reviewUsefulDao.deleteDislikeReview(reviewId, userId);
    }

    @Override
    public int getUsefulByReviewId(int reviewId) {
        return reviewUsefulDao.getUsefulByReviewId(reviewId);
    }
}
