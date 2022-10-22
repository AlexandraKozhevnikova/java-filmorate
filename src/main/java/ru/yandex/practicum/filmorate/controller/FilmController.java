package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Storage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Storage<Film> storage;

    @Autowired
    public FilmController(Storage<Film> storage) {
        this.storage = storage;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return storage.getAllItems();
    }

    @PostMapping
    public Film addNewFilm(@RequestBody @Valid AddFilmRequestDto filmDto) {
        Film film = FilmMapper.mapToFilm(filmDto);
        return storage.add(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid UpdateFilmRequestDto filmDto) {
        Film film = FilmMapper.mapToFilm(filmDto);
        return storage.update(film);
    }
}
