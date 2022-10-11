package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Storage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private Storage<Film> storage = new Storage();

    @GetMapping
    public List<Film> getAllFilms() {
        return storage.getAllItems();
    }

    @PostMapping
    public Film addNewFilm(@RequestBody @Valid Film film) {
        return storage.add(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return storage.update(film);
    }
}
