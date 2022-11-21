package ru.yandex.practicum.filmorate.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.web.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.web.mapper.GenreMapper;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final FilmService filmService;

    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Genre> getGenres() {
        List<Genre> list = filmService.getAllGenres();
        return list;
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable("id") int id) {
        Genre genre = filmService.getGenreById(id);
        return GenreMapper.mapGenreToGenreDto(genre);
    }

}
