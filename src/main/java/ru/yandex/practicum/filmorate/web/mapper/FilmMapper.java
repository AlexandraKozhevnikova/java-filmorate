package ru.yandex.practicum.filmorate.web.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.web.dto.Id;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmRequest;
import ru.yandex.practicum.filmorate.web.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.web.dto.film.UpdateFilmRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FilmMapper {
    public static Film mapToFilm(AddFilmRequest dto) {
        List<Integer> genres = Collections.emptyList();
        if (dto.getGenres() != null) {
            genres = dto.getGenres().stream()
                    .map(Id::getId)
                    .collect(Collectors.toList());
        }

        Film film = Film.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .genres(genres)
                .ratingMpaId(dto.getRatingMpaId().getId())
                .build();
        return film;
    }

    public static Film mapToFilm(UpdateFilmRequest dto) {
        List<Integer> genres = Collections.emptyList();
        if (dto.getGenres() != null) {
            genres = dto.getGenres().stream()
                    .map(Id::getId)
                    .collect(Collectors.toList());
        }

        Film film = Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .ratingMpaId(dto.getRatingMpaId().getId())
                .genres(genres)
                .build();
        return film;
    }

    public static FilmResponse mapFilmToFilmResponse(Film film) {
        List<Genre> genres = Collections.emptyList();
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genres = film.getGenres().stream()
                    .map(id -> Genre.getGenreById(id)
                            .orElseThrow(() -> new BadFoundResultByIdException("Genre with id = " + id + "does not exist")))
                    .collect(Collectors.toList());
        }

        RatingMpa ratingMpa = RatingMpa.getRatingMpaById(film.getRatingMpaId())
                .orElseThrow(() -> new BadFoundResultByIdException(
                        "RatingMPA with id = " + film.getRatingMpaId() + " does not exist"));

        return FilmResponse.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .ratingMpa(ratingMpa)
                .releaseDate(film.getReleaseDate())
                .genres(genres)
                .build();
    }
}
