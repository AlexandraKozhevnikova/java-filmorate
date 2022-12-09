package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:sql/schema.sql"})
class FilmControllerTest {

    private final FilmService service;

    @Test
    void deleteFilm() {

        List<Integer> genreList = new ArrayList<>();
        genreList.add(Genre.ACTION_MOVIE.getId());
        Film newFilm = new Film(0, "Test", "Test", LocalDate.now(), 1, 1, genreList);

        Film film = service.addFilm(newFilm);

        Film filmFromDB = service.getFilmById(film.getId());

        service.deleteFilm(filmFromDB.getId());

        Film deletedFilm = null;
        try {
            deletedFilm = service.getFilmById(film.getId());
        } catch (Throwable e) {
        }

        Assertions.assertNull(deletedFilm, "Фильм не удален");

    }
}