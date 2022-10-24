package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(Storage<Film> filmStorage, Storage<User> userStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllItems();
    }

    @PostMapping
    public Film addNewFilm(@RequestBody @Valid AddFilmRequestDto filmDto) {
        Film film = FilmMapper.mapToFilm(filmDto);
        return filmStorage.add(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid UpdateFilmRequestDto filmDto) {
        Film film = FilmMapper.mapToFilm(filmDto);
        return filmStorage.update(film);
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable("id") int id) {
        return filmStorage.getItemById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(
            @PathVariable("id") int id,
            @PathVariable("userId") int userId
    ) {
        userStorage.getItemById(userId);
        filmStorage.getItemById(id);
        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlike(
            @PathVariable("id") int id,
            @PathVariable("userId") int userId
    ) {
        userStorage.getItemById(userId);
        filmStorage.getItemById(id);
        filmService.unlike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTop(@RequestParam(name = "count", defaultValue = "10") int threshold){
        return filmService.getTopFilms(threshold);
    }
}
