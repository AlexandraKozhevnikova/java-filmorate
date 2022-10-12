package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Films", description="управление фильмами")
public class FilmController {

    private final Storage<Film> storage = new Storage();

    @GetMapping
    @Operation(
            summary = "Получение списка всех фильмов"
    )
    public List<Film> getAllFilms() {
        return storage.getAllItems();
    }

    @PostMapping
    @Operation(
            summary = "Добавление фильма",
            description = "нет проверки на уникальность"
    )
    public Film addNewFilm(@RequestBody @Valid Film film) {
        return storage.add(film);
    }

    @Operation(
            summary = "Обновление фильма",
            description = "для существующих фильмов"
    )
    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return storage.update(film);
    }
}
