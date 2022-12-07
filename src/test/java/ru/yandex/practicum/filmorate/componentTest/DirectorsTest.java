package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.DbFilmStorage;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DirectorsTest {
    private final DbFilmStorage filmStorage;
    private final FilmService filmService;

    @Test
    public void findDirectorByIdTest() {
        Director director = filmStorage.getDirectorById(1);
        assertThat(director)
                .hasFieldOrPropertyWithValue("name", "Стивен Спилберг");
    }

    @Test
    public void addDirectorTest() {
        Director director = Director.builder()
                .name("Джеймс Кэмерон")
                .build();

        director.setId(filmStorage.addDirector(director));

        Director directorFromDb = filmStorage.getDirectorById(director.getId());

        assertThat(directorFromDb)
                .hasFieldOrPropertyWithValue("name", "Джеймс Кэмерон");
    }

    @Test
    public void updateDirectorTest() {
        Director director = Director.builder()
                .id(1)
                .name("Джордж Лукас")
                .build();

        filmStorage.updateDirector(director);

        Director directorFromDb = filmStorage.getDirectorById(director.getId());

        assertThat(directorFromDb)
                .hasFieldOrPropertyWithValue("name", "Джордж Лукас");
    }

    @Test
    public void deleteDirectorTest() {
        filmStorage.deleteDirector(2);

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.getDirectorById(2)
        );
    }

    @Test
    public void getNotExistDirectorTest() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.getDirectorById(123)
        );
    }
}