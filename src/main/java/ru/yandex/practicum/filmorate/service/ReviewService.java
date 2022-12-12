package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.DbFilmStorage;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ReviewService {
    private final String SUCCESS = "success";
    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final DbFilmStorage filmStorage;

    public Review add(Review review) {
        int userId = review.getUserId();
        int filmId = review.getFilmId();
        userService.getUserById(userId);
        filmStorage.isExist(filmId);
        int id = reviewStorage.add(review);
        return getReviewById(id);
    }

    public Review update(Review review) {
        Optional<Review> existReview = reviewStorage.getReviewById(review.getId());
        if (existReview.isPresent()) {
            log.info("Review with id " + review.getId() + " has found");
            reviewStorage.update(review);
        } else {
            log.warn("Review can not be updated cause review with id = " + review.getId() + " not found");
            throw new NoSuchElementException
                    (
                            "Review can not be updated cause review with id = " + review.getId() + " not found"
                    );
        }
        return getReviewById(review.getId());
    }

    public void delete(int id) {
        reviewStorage.deleteReview(id);
    }

    public Review getReviewById(int id) {
        Optional<Review> review = reviewStorage.getReviewById(id);
        Review reviewFromDb = review.orElseThrow(
                () -> new NoSuchElementException("review with id = " + id + " not found")
        );
        int useful = reviewStorage.getUsefulByReviewId(reviewFromDb.getId());
        reviewFromDb.setUseful(useful);
        return reviewFromDb;
    }

    public List<Review> getAllReviews(Integer filmId, Integer count) {
        return reviewStorage.getReviewsByFilmId(filmId)
                .stream()
                .peek(review -> review.setUseful(reviewStorage.getUsefulByReviewId(review.getId())))
                .sorted(Comparator.comparing(Review::getUseful).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public String like(int reviewId, int userId) {
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        reviewStorage.likeReview(reviewId, userId);
        return SUCCESS;
    }

    public String deleteLike(int reviewId, int userId) {
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        reviewStorage.deleteLikeReview(reviewId, userId);
        return SUCCESS;
    }

    public String dislike(int reviewId, int userId) {
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        reviewStorage.dislikeReview(reviewId, userId);
        return SUCCESS;
    }

    public String deleteDislike(int reviewId, int userId) {
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        reviewStorage.deleteDislikeReview(reviewId, userId);
        return SUCCESS;
    }
}
