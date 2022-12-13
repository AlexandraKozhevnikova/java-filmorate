package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private final JdbcTemplate jdbcTemplate;

    private List<Film> createdFilms = new ArrayList<>();
    private List<User> createdUsers = new ArrayList<>();

    @BeforeEach
    public void dataPreparation() {
        User user = User.builder()
                .login("testuser")
                .email("testuser@ya.ru")
                .birthday(LocalDate.now())
                .build();

        userService.add(user);

        Film film = Film.builder()
                .name("test film for review")
                .duration(100)
                .ratingMpaId(1)
                .director(Collections.emptyList())
                .releaseDate(LocalDate.of(1965, 1, 12))
                .description("About the time")
                .genres(List.of(1))
                .build();

        filmService.addFilm(film);

        createdFilms.add(film);
        createdUsers.add(user);

        String SQL = "DELETE FROM feed;";
        jdbcTemplate.update(SQL);

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

    @AfterEach
    public void deleteTestData() {
        for (Film createdFilm : createdFilms) {
            filmService.deleteFilm(createdFilm.getId());
        }

        for (User createdUser : createdUsers) {
            userService.deleteUser(createdUser.getId());
        }

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
