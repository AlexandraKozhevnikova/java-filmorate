package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.NoSuchElementException;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:sql/schema.sql"})
class FilmDeleteTest {

    private final FilmService service;

    @Test
    void deleteExistFilm() {
        Film newFilm = Film.builder()
                .name("Test")
                .description("Test")
                .duration(150)
                .ratingMpaId(1)
                .releaseDate(LocalDate.now())
                .genres(Collections.emptyList())
                .director(Collections.emptyList())
                .build();

        Film film = service.addFilm(newFilm);
        Film filmFromDB = service.getFilmById(film.getId());
        service.deleteFilm(filmFromDB.getId());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> service.getFilmById(film.getId()),
                "фильм не удален из бд");
    }
}