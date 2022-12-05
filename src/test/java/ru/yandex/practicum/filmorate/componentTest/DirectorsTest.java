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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DirectorsTest {
    private final DbFilmStorage filmStorage;
    private final FilmService filmService;

    @Test
    public void findDirectorByIdTest() {
        Optional<Director> directorOptional = filmStorage.getDirectorById(1);

        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("id", 1)
                ).hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("name", "Стивен Спилберг")
                );
    }

    @Test
    public void addDirectorTest() {

        Director director = Director.builder()
                .name("Джеймс Кэмерон")
                .build();

        director.setId(filmStorage.addDirector(director));

        Optional<Director> directorOptional = filmStorage.getDirectorById(director.getId());

        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("name", "Джеймс Кэмерон")
                );
    }

    @Test
    public void updateDirectorTest() {

        Director director = Director.builder()
                .id(1)
                .name("Джордж Лукас")
                .build();

        filmStorage.updateDirector(director);

        Optional<Director> directorOptional = filmStorage.getDirectorById(1);

        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("name", "Джордж Лукас")
                );
    }

    @Test
    public void deleteDirectorTest() {
        filmStorage.deleteDirector(2);

        Optional<Director> directorOptional = filmStorage.getDirectorById(2);

        assertThat(directorOptional)
                .isEmpty();
    }

    @Test
    public void getNotExistDirectorTest() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.getDirectorById(123)
        );
    }
}