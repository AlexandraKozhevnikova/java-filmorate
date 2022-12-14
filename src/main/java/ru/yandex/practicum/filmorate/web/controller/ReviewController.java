package ru.yandex.practicum.filmorate.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.web.dto.review.AddReviewRequest;
import ru.yandex.practicum.filmorate.web.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.web.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.web.mapper.ReviewMapper;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ObjectMapper jacksonMapper = new ObjectMapper();

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponse addReview(@RequestBody @Valid AddReviewRequest reviewDto) {
        Review review = ReviewMapper.mapToReview(reviewDto);
        review = reviewService.add(review);
        return ReviewMapper.mapReviewToResponse(review);
    }

    @PutMapping
    public ReviewResponse updateReview(@RequestBody @Valid UpdateReviewRequest reviewDto) throws JsonProcessingException {
        log.info("Get request: PUT {}",
                Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        log.info("Request body: " + jacksonMapper.writeValueAsString(reviewDto));
        Review review = ReviewMapper.mapToReview(reviewDto);
        review = reviewService.update(review);
        ReviewResponse responseBody = ReviewMapper.mapReviewToResponse(review);
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(responseBody));
        return responseBody;
    }

    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable("id") int id) {
        Review review = reviewService.getReviewById(id);
        reviewService.delete(review);
        return "success";
    }

    @GetMapping("/{id}")
    public ReviewResponse getReviewById(@PathVariable("id") int id) {
        Review review = reviewService.getReviewById(id);
        return ReviewMapper.mapReviewToResponse(review);
    }

    @GetMapping
    public List<ReviewResponse> getAllReviews(
            @RequestParam(required = false, name = "filmId") Integer filmId,
            @RequestParam(name = "count", defaultValue = "10") Integer count
    ) {
        List<Review> reviews = reviewService.getAllReviews(filmId, count);
        return reviews.stream()
                .map(ReviewMapper::mapReviewToResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/like/{userId}")
    public String like(
            @PathVariable("id") int reviewId,
            @PathVariable("userId") int userId
    ) {
        return reviewService.like(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public String dislike(
            @PathVariable("id") int reviewId,
            @PathVariable("userId") int userId
    ) {
        return reviewService.dislike(reviewId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLike(
            @PathVariable("id") int reviewId,
            @PathVariable("userId") int userId
    ) {
        return reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public String deleteDislike(
            @PathVariable("id") int reviewId,
            @PathVariable("userId") int userId
    ) {
        return reviewService.deleteDislike(reviewId, userId);
    }
}
