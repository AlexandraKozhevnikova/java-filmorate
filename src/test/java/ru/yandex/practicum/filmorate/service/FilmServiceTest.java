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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FilmServiceTest {
    private UserStorage userStorage;
    private User user;
    private FilmStorage filmStorage;
    private Film film;
    private FilmService service;


    @BeforeEach
    public void dataPreparation() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        service = new FilmService(filmStorage, userStorage);

        user = new User();
        user.setLogin("login");
        user.setEmail("log@ya.ru");
        user.setBirthday(LocalDate.of(1999, 1, 1));
        userStorage.add(user);

        film = new Film();
        film.setName("the best");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        filmStorage.add(film);
    }

    @Test
    public void shouldLikedUnlikedFilm() {
        service.like(film.getId(), user.getId());
        assertEquals(List.of(film), service.getTopFilms(2));
        service.unlike(film.getId(), user.getId());
        assertTrue(service.getTopFilms(2).isEmpty());
    }
}