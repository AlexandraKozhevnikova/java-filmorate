package ru.yandex.practicum.filmorate.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final FilmService filmService;

    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<RatingMPA> getMpa() {
        List<RatingMPA> list = filmService.getAllMpa();
        return list;
    }

    @GetMapping("/{id}")
    public RatingMPA getMpaById(@PathVariable("id") int id) {
        return filmService.getMpaById(id);
    }
}
