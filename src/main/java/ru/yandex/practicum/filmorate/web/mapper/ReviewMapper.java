package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.web.dto.review.AddReviewRequest;
import ru.yandex.practicum.filmorate.web.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.web.dto.review.UpdateReviewRequest;

public class ReviewMapper {
    public static Review mapToReview(AddReviewRequest dto) {
        return Review.builder()
                .content(dto.getContent())
                .isPositive(dto.getIsPositive())
                .userId(dto.getUserId())
                .filmId(dto.getFilmId())
                .useful(0)
                .build();
    }

    public static Review mapToReview(UpdateReviewRequest dto) {
        return Review.builder()
                .id(dto.getReviewId())
                .content(dto.getContent())
                .isPositive(dto.getIsPositive())
                .filmId(dto.getFilmId())
                .userId(dto.getUserId())
                .build();
    }

    public static ReviewResponse mapReviewToResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .useful(review.getUseful())
                .build();
    }
}
