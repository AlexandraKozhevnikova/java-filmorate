package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FilmServiceTest {
    private UserStorage userStorage;
    private User user1;
    private User user2;
    private FilmStorage filmStorage;
    private Film film1;
    private Film film2;
    private Film film3;
    private FilmService service;


    @BeforeEach
    public void dataPreparation() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        service = new FilmService(filmStorage, userStorage);

        user1 = new User();
        user1.setLogin("login");
        user1.setEmail("log@ya.ru");
        user1.setBirthday(LocalDate.of(1999, 1, 1));
        userStorage.add(user1);

        user2 = new User();
        user2.setLogin("log22in2");
        user2.setEmail("lo2g@ya.ru");
        user2.setBirthday(LocalDate.of(1999, 1, 1));
        userStorage.add(user2);

        film1 = new Film();
        film1.setName("the best");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        filmStorage.add(film1);

        film2 = new Film();
        film2.setName("the worst");
        film2.setDuration(140);
        film2.setReleaseDate(LocalDate.of(2002, 2, 2));
        filmStorage.add(film2);

        film3 = new Film();
        film3.setName("the worst");
        film3.setDuration(140);
        film3.setReleaseDate(LocalDate.of(2002, 2, 2));
        filmStorage.add(film3);
    }

    @Test
    public void shouldLikedUnlikedFilmAndGetInRightOrder() {
        service.like(film2.getId(), user1.getId());
        service.like(film2.getId(), user2.getId());
        service.like(film1.getId(), user1.getId());
        service.like(film1.getId(), user1.getId());
        assertEquals(3, service.getTopFilms(10).size());
        assertEquals(film2, service.getTopFilms(10).stream().findFirst().get());
        service.unlike(film2.getId(), user1.getId());
        service.unlike(film2.getId(), user2.getId());
        assertEquals(3, service.getTopFilms(10).size());
        assertEquals(Set.of(film1), service.getTopFilms(1));
    }
}