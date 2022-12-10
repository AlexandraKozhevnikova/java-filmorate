package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommonFilmsTest {

    private final FilmService service;

    @Test
    public void shouldReturnCommonFilms() {
        List<Film> films = service.getCommonFilms(1, 2);
        Assertions.assertIterableEquals(
                List.of(11, 2, 1),
                films.stream()
                        .map(Film::getId)
                        .collect(Collectors.toList())
        );
    }
}
