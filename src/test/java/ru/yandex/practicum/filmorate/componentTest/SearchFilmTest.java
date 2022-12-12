package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.web.dto.SearchByType;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SearchFilmTest {
    private final FilmService service;

    @Test
    public void shouldReturnFilmsWithTitleInPopularOrder() {
        List<Film> films = service.search("форс", List.of(SearchByType.NAME));
        Assertions.assertIterableEquals(
                List.of(14, 13),
                films.stream()
                        .map(Film::getId)
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void shouldReturnFilmWithDirector() {
        List<Film> films = service.search("роб", List.of(SearchByType.DIRECTOR));
        Assertions.assertIterableEquals(
                List.of(14, 13),
                films.stream()
                        .map(Film::getId)
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void shouldReturnFilmWithTitleAndDirectorInPopularOrder() {
        List<Film> films = service.search("младший", List.of(SearchByType.DIRECTOR, SearchByType.NAME));
        Assertions.assertIterableEquals(
                List.of(15, 16),
                films.stream()
                        .map(Film::getId)
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void shouldReturnEmtyList() {
        List<Film> films = service.search(
                "не найдется по такому запросу",
                List.of(SearchByType.DIRECTOR, SearchByType.NAME)
        );
        Assertions.assertTrue(films.isEmpty());
    }
}
