package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.BadReviewReactionException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewServiceTest {
    private final ReviewService reviewService;
    private final UserService userService;
    private final FilmService filmService;
    final JdbcTemplate jdbcTemplate;

    @Test
    public void getReviewById() {
        Review review = reviewCreation();
        Review reviewFromDb = reviewService.getReviewById(1);

        Assertions.assertEquals(review, reviewFromDb);
    }

    @Test
    public void addTest() {
        Review review = reviewCreation();

        Assertions.assertEquals(1, review.getId());
    }

    @Test
    public void updateTest() {
        Review newReview = Review.builder()
                .id(1)
                .content("just movie")
                .userId(1)
                .filmId(1)
                .isPositive(true)
                .build();
        Review updatedReview = reviewService.update(newReview);

        Assertions.assertEquals(newReview, updatedReview);
    }

    @Test
    public void deleteTest() {
        reviewService.delete(1);

        Throwable thrown = assertThrows(NoSuchElementException.class, () -> reviewService.getReviewById(1));
        String errorMessage = thrown.getMessage();
        assertEquals("review with id = 1 not found", errorMessage);
    }

    @Test
    public void getAllReviewsTest() {
//        Для теста создадим ещё один отзыв.
        Review secondReview = reviewCreation();
        secondReview.setContent("Super");
        reviewService.add(secondReview);

//        Добавляем лайк отзыву с id = 2 для проверки сортировки.
        reviewService.like(2, 1);
        secondReview = reviewService.getReviewById(2);

//        Выводим полный список. Должно быть 2 отзыва, при этом второй отзыв будет первым, потому что более полезный.
        List<Review> reviews = reviewService.getAllReviews(1, 2);

        Assertions.assertEquals(2, reviews.size());
        Assertions.assertEquals(secondReview, reviews.get(0));
//    Выводим только один отзыв.
        reviews = reviewService.getAllReviews(1, 1);

        Assertions.assertEquals(1, reviews.size());
    }

    @Test
    public void likeTest() {
        reviewService.like(1, 1);
        Review review = reviewService.getReviewById(1);
//     Полезность должна вырасти на 1.
        Assertions.assertEquals(1, review.getUseful());
//      Повторно лайкнуть нельзя, будет ошибка.
        assertThrows(DuplicateKeyException.class, () -> reviewService.like(1, 1));
    }

    @Test
    public void deleteLikeTest() {
//        Нельзя удалить несуществующий лайк.
        assertThrows(BadReviewReactionException.class, () -> reviewService.deleteLike(1, 1));
//        Ставим и удаляем лайк. Получаем полезность = 0.
        reviewService.like(1, 1);
        reviewService.deleteLike(1, 1);
        Review review = reviewService.getReviewById(1);

        Assertions.assertEquals(0, review.getUseful());
    }

    @Test
    public void dislikeTest() {
        reviewService.dislike(1, 1);
        Review review = reviewService.getReviewById(1);
//     Полезность должна уменьшиться на 1.
        Assertions.assertEquals(-1, review.getUseful());
//      Повторно дизлайкнуть нельзя, будет ошибка.
        assertThrows(DuplicateKeyException.class, () -> reviewService.like(1, 1));
    }

    @Test
    public void deleteDislikeTest() {
//        Нельзя удалить несуществующий дизлайк.
        assertThrows(BadReviewReactionException.class, () -> reviewService.deleteDislike(1, 1));
//        Ставим и удаляем дизлайк. Получаем полезность = 0.
        reviewService.dislike(1, 1);
        reviewService.deleteDislike(1, 1);
        Review review = reviewService.getReviewById(1);

        Assertions.assertEquals(0, review.getUseful());
    }

    private Review reviewCreation() {
        return Review.builder()
                .id(1)
                .content("best movie")
                .userId(1)
                .filmId(1)
                .isPositive(true)
                .build();
    }

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
                .releaseDate(LocalDate.of(1965, 1, 12))
                .description("About the time")
                .genres(List.of(1, 2, 3))
                .build();

        filmService.addFilm(film);
        Review review = reviewCreation();
        reviewService.add(review);
    }

    @AfterEach
    public void clearDb() {
        String sql = "DELETE FROM USER_FILMORATE; " +
                "ALTER TABLE USER_FILMORATE ALTER COLUMN ID RESTART START WITH 1;" +
                "DELETE from FILM; " +
                "ALTER TABLE FILM ALTER COLUMN ID RESTART START WITH 1;" +
                "DELETE from REVIEW; " +
                "ALTER TABLE REVIEW ALTER COLUMN REVIEW_ID RESTART START WITH 1;";
        jdbcTemplate.update(sql);
    }
}
