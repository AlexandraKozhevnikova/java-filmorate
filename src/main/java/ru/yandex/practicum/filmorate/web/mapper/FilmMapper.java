package ru.yandex.practicum.filmorate.web.mapper;

import lombok.extern.log4j.Log4j2;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.web.dto.Id;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.web.dto.film.UpdateFilmRequestDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class FilmMapper {
    public static Film mapToFilm(AddFilmRequestDto dto) {
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

    public static Film mapToFilm(UpdateFilmRequestDto dto) {
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

    public static FilmResponseDto mapFilmToFilmResponseDto(Film film) {
        List<Genre> genres = Collections.emptyList();
        if (film.getGenres() != null && !film.getGenres().isEmpty()) { //todo   подебажить на пустой и нал
            genres = film.getGenres().stream()
                    .map(id -> Genre.getGenreById(id)
                            .orElseThrow(() -> new BadFoundResultByIdException("Genre with id = " + id + "does not exist")))
                    .collect(Collectors.toList());
        }

        RatingMpa ratingMpa = RatingMpa.getRatingMpaById(film.getRatingMpaId())
                .orElseThrow(() -> new BadFoundResultByIdException(
                        "RatingMPA with id = " + film.getRatingMpaId() + " does not exist"));

        return FilmResponseDto.builder()
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
