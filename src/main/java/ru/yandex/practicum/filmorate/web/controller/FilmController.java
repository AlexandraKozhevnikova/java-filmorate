package ru.yandex.practicum.filmorate.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.web.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.web.mapper.FilmMapper;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Log4j2
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final ObjectMapper jacksonMapper = new ObjectMapper();


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmResponseDto> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        List<FilmResponseDto> response = films.stream()
                .map(FilmMapper::mapFilmToFilmResponseDto)
                .collect(Collectors.toList());
        return response;
    }

    @PostMapping
    public FilmResponseDto addFilm(@RequestBody @Valid AddFilmRequestDto filmDto) {
        Film filmFromRequest = FilmMapper.mapToFilm(filmDto);
        Film savedFilm = filmService.addFilm(filmFromRequest);
        return FilmMapper.mapFilmToFilmResponseDto(savedFilm);
    }

    @PutMapping
    public FilmResponseDto updateFilm(@RequestBody @Valid UpdateFilmRequestDto filmDto) throws JsonProcessingException {
        log.info(
                "Get request: PUT {}",
                Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get()
        );
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(filmDto));
        Film film = FilmMapper.mapToFilm(filmDto);
        Film savedFilm = filmService.update(film);
        return FilmMapper.mapFilmToFilmResponseDto(savedFilm);
    }

    @GetMapping("/{id}")
    public FilmResponseDto getFilmById(@PathVariable("id") int id) {
        Film film = filmService.getFilmById(id);
        return FilmMapper.mapFilmToFilmResponseDto(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        filmService.unlike(id, userId);
    }

    @GetMapping("/popular")
    public Set<Film> getTop(
            @RequestParam(name = "count", defaultValue = "10")
            @Min(value = 1, message = "'count' should be positive")
            Integer threshold
    ) {
        return filmService.getTopFilms(threshold);
    }
}
