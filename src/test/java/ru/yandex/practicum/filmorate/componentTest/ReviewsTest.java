package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import ru.yandex.practicum.filmorate.exception.BadReviewReactionException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewsTest {
    private final ReviewService reviewService;
    private final UserService userService;
    private final FilmService filmService;

    @BeforeEach
    public void dataPreparation() {
        User user = User.builder()
                .login("pinki")
                .email("pinki@ya.ru")
                .birthday(LocalDate.now())
                .build();

        userService.add(user);

        Film film = Film.builder()
                .name("the time")
                .duration(100)
                .ratingMpaId(1)
                .director(Collections.emptyList())
                .releaseDate(LocalDate.of(1965, 1, 12))
                .description("About the time")
                .genres(List.of(1, 2, 3))
                .build();

        filmService.addFilm(film);
        Review review = reviewCreation();
        reviewService.add(review);
    }

    @Test
    public void addAndGetReviewByIdTest() {
        Review review = reviewCreation();
        Review reviewFromDb = reviewService.getReviewById(review.getId());

        Assertions.assertEquals(review, reviewFromDb);
    }

    @Test
    public void updateTest() {
        Review review = reviewCreation();

        Review newReview = Review.builder()
                .id(review.getId())
                .content("just movie")
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .isPositive(review.getIsPositive())
                .build();

        Review updatedReview = reviewService.update(newReview);

        Assertions.assertEquals(newReview, updatedReview);
    }

    @Test
    public void deleteTest() {
        Review review = reviewCreation();

        reviewService.delete(review);

        Throwable thrown = assertThrows(NoSuchElementException.class, () -> reviewService.getReviewById(review.getId()));
        String errorMessage = thrown.getMessage();
        assertEquals("review with id = " + review.getId() + " not found", errorMessage);
    }

    @Test
    public void getAllReviewsTest() {
        // Для теста создадим ещё один отзыв.
        Review newReview = reviewCreation();
        newReview.setContent("Super");
        reviewService.add(newReview);
        int newReviewId = newReview.getId();

        // Добавляем лайк отзыву с id = 2 для проверки сортировки.
        reviewService.like(newReviewId, newReview.getUserId());
        newReview = reviewService.getReviewById(newReviewId);

        //        Выводим полный список. Должно быть 2 отзыва, при этом второй отзыв будет первым, потому что более полезный.
        List<Review> reviews = reviewService.getAllReviews(newReview.getFilmId(), 2);

        Assertions.assertEquals(2, reviews.size());
        Assertions.assertEquals(newReview, reviews.get(0));

        //    Выводим только один отзыв.
        reviews = reviewService.getAllReviews(1, 1);

        Assertions.assertEquals(1, reviews.size());
    }

    @Test
    public void likeTest() {
        Review review = reviewCreation();
        reviewService.like(review.getId(), review.getUserId());
        Review reviewFromDb = reviewService.getReviewById(review.getId());
        // Полезность должна вырасти на 1.
        Assertions.assertEquals(1, reviewFromDb.getUseful());
        //   Повторно лайкнуть нельзя, будет ошибка.
        assertThrows(DuplicateKeyException.class, () -> reviewService.like(review.getId(), review.getUserId()));
    }

    @Test
    public void deleteLikeTest() {
        //   Нельзя удалить несуществующий лайк.
        assertThrows(BadReviewReactionException.class, () -> reviewService.deleteLike(1, 1));
        //   Ставим и удаляем лайк. Получаем полезность = 0.
        reviewService.like(1, 1);
        reviewService.deleteLike(1, 1);
        Review review = reviewService.getReviewById(1);

        Assertions.assertEquals(0, review.getUseful());
    }

    @Test
    public void dislikeTest() {
        Review review = reviewCreation();
        reviewService.dislike(review.getId(), review.getUserId());
        Review reviewFromDb = reviewService.getReviewById(review.getId());
        //     Полезность должна уменьшиться на 1.
        Assertions.assertEquals(-1, reviewFromDb.getUseful());
        //      Повторно дизлайкнуть нельзя, будет ошибка.
        assertThrows(DuplicateKeyException.class, () ->
                reviewService.like(reviewFromDb.getId(), reviewFromDb.getUserId()));
    }

    @Test
    public void deleteDislikeTest() {
        //        Нельзя удалить несуществующий дизлайк.
        Review review = reviewCreation();
        int reviewId = review.getId();
        int userId = review.getUserId();

        assertThrows(BadReviewReactionException.class, () ->
                reviewService.deleteDislike(reviewId, userId));
        //        Ставим и удаляем дизлайк. Получаем полезность = 0.
        reviewService.dislike(reviewId, userId);
        reviewService.deleteDislike(reviewId, userId);
        Review reviewFromDb = reviewService.getReviewById(reviewId);

        Assertions.assertEquals(0, reviewFromDb.getUseful());
    }

    private Review reviewCreation() {
        Review review = Review.builder()
                .id(1)
                .content("best movie")
                .userId(1)
                .filmId(1)
                .isPositive(true)
                .build();
        return reviewService.add(review);
    }
}
