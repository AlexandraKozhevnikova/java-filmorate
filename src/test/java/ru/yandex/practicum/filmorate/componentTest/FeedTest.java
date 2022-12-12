package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FeedTest {

    private final ReviewService reviewService;
    private final UserService userService;
    private final FilmService filmService;

    private final FeedService feedService;

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
    }

    @Test
    public void addReviewAndGetFeed() {
        Review review = reviewCreation();
        List<Feed> feedList = feedService.getFeedById(review.getUserId());
        Assertions.assertEquals(1, feedList.size(), "Список ленты пустой");
        Feed feed = feedList.get(0);
        Assertions.assertEquals(EventType.REVIEW.getName(), feed.getEventType(), "Ошибка типа");
        Assertions.assertEquals(Operation.ADD.getName(), feed.getOperation(), "Ошибка операции");
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
