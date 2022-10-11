package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Storage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StorageTest {

    @DisplayName("Проверить номера id для разных стораджей")
    @Test
    public void shouldSaveFilmWithId() {
        Storage<Film> storage = new Storage();
        Film filmFirst = new Film("name1", "descr1", LocalDate.now(), 200);
        storage.add(filmFirst);
        Film filmSecond = new Film("name2", "descr2", LocalDate.now(), 200);
        storage.add(filmSecond);

        assertEquals(1, filmFirst.getId());
        assertEquals(2, filmSecond.getId());

        Storage otherStorage = new Storage();
        Film film = new Film("name", "descr", LocalDate.now(), 200);
        otherStorage.add(film);

        assertEquals(1, film.getId());
    }

    @DisplayName("Сохранить два фильма в один сторадж")
    @Test
    public void shouldAddFilmInStorage() {
        Storage<Film> storage = new Storage();
        Film filmFirst = new Film("name1", "descr1", LocalDate.now(), 200);
        storage.add(filmFirst);
        Film filmSecond = new Film("name2", "descr2", LocalDate.now(), 200);
        storage.add(filmSecond);

        assertEquals(2, storage.getAllItems().size());
    }

    @DisplayName("Обновить несуществующий фильм")
    @Test
    public void shouldGetExceptionWhenUpdateFilm() {
        Storage<Film> storage = new Storage();
        assertTrue(storage.getAllItems().isEmpty());

        Film film = new Film("name2", "descr2", LocalDate.now(), 200);
        film.setId(4);

        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> storage.update(film)
        );

        assertEquals("Не найден элемент с id 4", exception.getMessage());
    }

    @DisplayName("Успешно обновить фильм")
    @Test
    public void shouldSuccessUpdatedFilm() {
        Storage<Film> storage = new Storage();
        Film filmFirst = new Film("name old", "descr1", LocalDate.now(), 200);
        storage.add(filmFirst);
        assertEquals(1, filmFirst.getId());
        assertEquals(1, storage.getAllItems().size());

        assertEquals("name old", storage.getAllItems().get(0).getName());

        Film filmSecond = new Film("name new", "descr2", LocalDate.now(), 200);
        filmSecond.setId(1);
        storage.update(filmSecond);

        assertEquals(1, storage.getAllItems().size());
        assertEquals("name new", storage.getAllItems().get(0).getName());
    }


}
