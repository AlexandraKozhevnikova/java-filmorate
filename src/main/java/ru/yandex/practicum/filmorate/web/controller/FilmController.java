package ru.yandex.practicum.filmorate.web.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import ru.yandex.practicum.filmorate.web.dto.SearchByType;
import ru.yandex.practicum.filmorate.web.dto.SortTypeDirectors;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmRequest;
import ru.yandex.practicum.filmorate.web.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.web.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.web.mapper.FilmMapper;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService ;
    private final ObjectMapper jacksonMapper = new ObjectMapper();


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmResponse> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        return films.stream()
                .map(FilmMapper::mapFilmToFilmResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public FilmResponse addFilm(@RequestBody @Valid AddFilmRequest filmDto) {
        Film filmFromRequest = FilmMapper.mapToFilm(filmDto);
        Film savedFilm = filmService.addFilm(filmFromRequest);
        return FilmMapper.mapFilmToFilmResponse(savedFilm);
    }

    @PutMapping
    public FilmResponse updateFilm(@RequestBody @Valid UpdateFilmRequest filmDto) throws JsonProcessingException {
        log.info("Get request: PUT {}",
                Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(filmDto));
        Film film = FilmMapper.mapToFilm(filmDto);
        Film savedFilm = filmService.update(film);
        return FilmMapper.mapFilmToFilmResponse(savedFilm);
    }

    @GetMapping("/{id}")
    public FilmResponse getFilmById(@PathVariable("id") int id) {
        Film film = filmService.getFilmById(id);
        return FilmMapper.mapFilmToFilmResponse(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public String like(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        return filmService.like(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String unlike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmService.unlike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmResponse> getTop(
            @RequestParam(name = "count", defaultValue = "10")
            @Min(value = 1, message = "'count' should be positive")
            Integer threshold,
            @RequestParam(name = "genreId", required = false)
            Integer genreId,
            @RequestParam(name = "year", required = false)
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
            String year
    ) {
        List<Film> films = filmService.getTopFilms(threshold, genreId, year);
        return films.stream()
                .map(FilmMapper::mapFilmToFilmResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/director/{directorId}")
    public List<FilmResponse> getAllFilmsByDirector(
            @PathVariable("directorId") int directorId,
            @RequestParam(name = "sortBy", defaultValue = "likes")
            SortTypeDirectors sortTypeForDirector
    ) {
        List<Film> films = filmService.getAllFilmsByDirector(directorId,
                sortTypeForDirector);
        return films.stream()
                .map(FilmMapper::mapFilmToFilmResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<FilmResponse> searchFilms(
            @NotBlank
            @RequestParam(name = "guery")
            String  query,
            @NotBlank
            @RequestParam(name = "by")
            SearchByType searchBy
            //todo переделать на енам когда вмержать режиссеров https://www.baeldung.com/spring-enum-request-param
    ) {
        List<Film> films = filmService.search(query, searchBy);
        return films.stream()
                .map(FilmMapper::mapFilmToFilmResponse)
                .collect(Collectors.toList());
    }
}
