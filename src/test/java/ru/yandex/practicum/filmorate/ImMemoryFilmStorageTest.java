package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Optional;

public class ImMemoryFilmStorageTest {
    private FilmStorage storage = new InMemoryFilmStorage();

    @Test
    public void addFilmTest() {
        Film film = Film.builder()
                .name("the time")
                .duration(100)
                .ratingMpaId(1)
                .build();

        int id = storage.add(film);
        Assertions.assertEquals(1, id);
    }

    @Test
    public void updateFilmTest() {
        Film film = Film.builder()
                .id(1)
                .name("the GREAT time")
                .duration(100)
                .ratingMpaId(1)
                .build();

        storage.update(film);
        Assertions.assertEquals(
                "the GREAT time",
                storage.getItemById(1).get().getName());
    }

    @Test
    public void getNotExistFilmTest() {
        Optional<Film> film;

        film = storage.getItemById(123);
        Assertions.assertFalse(film.isPresent());
    }

    @Test
    public void getExistFilmTest() {
        dataPreparation();
        Optional<Film> film;

        film = storage.getItemById(1);
        Assertions.assertTrue(film.isPresent());
        Assertions.assertEquals("the time", film.get().getName());
    }

    @Test
    public void getAllFilm() {
        dataPreparation();
        dataPreparation();
        dataPreparation();

        Assertions.assertEquals(3, storage.getAllItems().size());
    }

    private void dataPreparation() {
        Film film = Film.builder()
                .name("the time")
                .duration(100)
                .ratingMpaId(1)
                .build();

        storage.add(film);
    }
}
