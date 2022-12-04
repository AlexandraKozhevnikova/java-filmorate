package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
public class RecommendationsTest {

    public Film film1 = Film.builder()
            .name("test1")
            .description("desc")
            .duration(50)
            .releaseDate(LocalDate.of(2000, 1, 1))
            .build();
    public Film film2 = Film.builder()
            .name("test2")
            .description("desc")
            .duration(50)
            .releaseDate(LocalDate.of(2000, 1, 1))
            .build();

    public Film film3 = Film.builder()
            .name("test3")
            .description("desc")
            .duration(50)
            .releaseDate(LocalDate.of(2000, 1, 1))
            .build();

    public User user1 = User.builder()
            .email("is@email.com")
            .login("test1")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();
    public User user2 = User.builder()
            .email("is@email.com")
            .login("test2")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();
    public User user3 = User.builder()
            .email("is@email.com")
            .login("test3")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();
    
    public final JdbcTemplate jdbcTemplate;
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;
    public final UserService userService;

    @Autowired
    public RecommendationsTest(JdbcTemplate jdbcTemplate, @Qualifier("dbFilmStorage") FilmStorage filmStorage, @Qualifier("dbUserStorage") UserStorage userStorage, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.userService = userService;
    }
    
    @BeforeEach
    void getReady() {
        filmStorage.add(film1);
        filmStorage.add(film2);
        filmStorage.add(film3);
        userStorage.add(user1);
        userStorage.add(user2);
        userStorage.add(user3);
    }
    
    @AfterEach
    void reset() {
        /*
        jdbcTemplate.update("DELETE FROM film;");
        jdbcTemplate.update("DELETE FROM user_filmorate;");

         */
    }
    
    @Test
    void testRecommendations() {
        filmStorage.likeFilm(1,1);
        filmStorage.likeFilm(1,2);

        // ALL LIKE ONE FILM
        assertEquals(0, userService.getFilmRecommendations(1).size());

        // ONE RECOMMENDED
        filmStorage.likeFilm(2,2);
        assertEquals(1, userService.getFilmRecommendations(1).size());

        // ADD MORE LIKES AND SHOW FILM FROM OTHER USER'S RECOMMENDATION
        filmStorage.likeFilm(2,1);
        filmStorage.likeFilm(1,3);
        filmStorage.likeFilm(2,3);
        filmStorage.likeFilm(3,3);

        assertEquals(1, userService.getFilmRecommendations(1).size());
        assertEquals("test3", userService.getFilmRecommendations(1).get(0).getName());

        // NO LIKES AND OVERLAPS
        filmStorage.unlikeFilm(1,1);
        filmStorage.unlikeFilm(2,1);
        assertEquals(0, userService.getFilmRecommendations(1).size());
    }
}
