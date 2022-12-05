package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GetPopularTest {
    private final FilmService filmService;

    @Test
    public void shouldGetEmptyListTest() {
        List<Film> films = filmService.getTopFilms(10, 5, "3030");
        Assertions.assertTrue(films.isEmpty(), "список не пустой");
    }

    @Test
    public void shouldGetAllComedyFilmTest() {
        List<Film> films = filmService.getTopFilms(10, 1, null);
        Assertions.assertEquals(4, films.size(), "размер списка комедий должен быть 4");
    }

    @Test
    public void shouldGetTopPopularComedyTest() {
        List<Film> films = filmService.getTopFilms(3, 1, null);
        Assertions.assertEquals(3, films.size(), "размер списка комедий должен быть 3");
        Assertions.assertEquals("один дома 2", films.get(0).getName());
        Assertions.assertEquals("один дома 1", films.get(1).getName());
        Assertions.assertEquals("один дома 3", films.get(2).getName());
    }

    @Test
    public void shouldGetTopPopularComedyWithYearTest() {
        List<Film> films = filmService.getTopFilms(3, 1, "2000");
        Assertions.assertEquals(2, films.size(), "размер списка комедий должен быть 2");
        Assertions.assertEquals("один дома 2", films.get(0).getName());
        Assertions.assertEquals("один дома 1", films.get(1).getName());
    }

    @Test
    public void shouldGetWithoutGenreIdWithYearTest() {
        List<Film> films = filmService.getTopFilms(6, null, "2022");
        Assertions.assertEquals(5, films.size(), "размер списка комедий должен быть 5");
        Assertions.assertEquals("один дома 11", films.get(0).getName());
        Assertions.assertEquals("один дома 10", films.get(1).getName());
        Assertions.assertEquals(LocalDate.parse("2022-10-10"), films.get(2).getReleaseDate());
        Assertions.assertEquals(LocalDate.parse("2022-10-10"), films.get(3).getReleaseDate());
        Assertions.assertEquals(LocalDate.parse("2022-10-10"), films.get(4).getReleaseDate());
    }

    @Test
    public void shouldGetTenItems() {
        List<Film> films = filmService.getTopFilms(10, null, null);
        Assertions.assertEquals(10, films.size(), "размер списка комедий должен быть 10");
        Assertions.assertEquals("один дома 11", films.get(0).getName());
        Assertions.assertEquals("один дома 2", films.get(1).getName());
        Assertions.assertEquals("один дома 1", films.get(2).getName());
        Assertions.assertEquals("один дома 3", films.get(3).getName());
    }
}
