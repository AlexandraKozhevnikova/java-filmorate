package ru.yandex.practicum.filmorate.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.web.dto.director.AddDirectorRequest;
import ru.yandex.practicum.filmorate.web.dto.director.DirectorResponse;
import ru.yandex.practicum.filmorate.web.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.web.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.web.mapper.FilmMapper;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final FilmService filmService;

    private final ObjectMapper jacksonMapper = new ObjectMapper();

    public DirectorController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<DirectorResponse> getAllDirectors() {
        List<Director> list = filmService.getAllDirectors();
        return list.stream()
                .map(DirectorMapper::mapDirectorToDirectorResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public DirectorResponse addDirector(@RequestBody @Valid AddDirectorRequest directorDto) {
        Director directorFromRequest = DirectorMapper.mapToDirector(directorDto);
        Director savedDirector = filmService.addDirector(directorFromRequest);
        return DirectorMapper.mapDirectorToDirectorResponse(savedDirector);
    }

    @PutMapping
    public DirectorResponse updateFilm(@RequestBody @Valid UpdateDirectorRequest directorDto) throws JsonProcessingException {
        log.info("Get request: PUT {}",
                Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(directorDto));
        Director director = DirectorMapper.mapToDirector(directorDto);
        Director savedDirector = filmService.updateDirector(director);
        return DirectorMapper.mapDirectorToDirectorResponse(savedDirector);
    }

    @GetMapping("/{id}")
    public DirectorResponse getDirectorById(@PathVariable("id") int id) {
        Director director = filmService.getDirectorById(id);
        return DirectorMapper.mapDirectorToDirectorResponse(director);
    }

    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable("id") int id) {
        log.info("Get request: DELETE {}",
                Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        log.info("Response status code: 200 ОК");
        filmService.deleteDirector(id);
    }
}
