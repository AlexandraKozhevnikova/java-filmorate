package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.web.dto.RatingMpaId;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmResponseDto;
import ru.yandex.practicum.filmorate.web.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.film.UpdateFilmResponseDto;
import ru.yandex.practicum.filmorate.web.dto.genre.GenreIdDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FilmMapper {
    public static Film mapToFilm(AddFilmRequestDto dto) {
        List<Integer> genres = Collections.emptyList();
        if (dto.getGenres() != null) {
            genres = dto.getGenres().stream()
                    .map(GenreIdDto::getId)
                    .collect(Collectors.toList());
        }

        Film film = Film.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .genres(genres)
                .ratingMPA(RatingMPA.getRatingMpaById(dto.getRatingMpaId().getId()))
                .build();
        return film;
    }

    public static Film mapToFilm(UpdateFilmRequestDto dto) {
        List<Integer> genres = Collections.emptyList();
        if (dto.getGenres() != null) {
            genres = dto.getGenres().stream()
                    .map(GenreIdDto::getId)
                    .collect(Collectors.toList());
        }

        Film film = Film.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .id(dto.getId())
                .ratingMPA(RatingMPA.getRatingMpaById(dto.getRatingMpaId().getId()))
                .genres(genres)
                .build();
        return film;
    }

    public static AddFilmResponseDto mapFilmToAddFilmResponseDto(Film film) {
        List<GenreIdDto> genres = Collections.emptyList();

        if (!film.getGenres().isEmpty()) {
            genres = film.getGenres().stream()
                    .map(GenreIdDto::new)
                    .collect(Collectors.toList());
        }

        return AddFilmResponseDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .ratingMpaId(new RatingMpaId(film.getRatingMPA().getId()))
                .releaseDate(film.getReleaseDate())
                .genres(genres)
                .build();
    }

    public static UpdateFilmResponseDto mapFilmToUpdateFilmResponseDto(Film film) {
        List<GenreIdDto> genres = Collections.emptyList();

        if (!film.getGenres().isEmpty()) {
            genres = film.getGenres().stream()
                    .map(GenreIdDto::new)
                    .collect(Collectors.toList());
        }

        UpdateFilmResponseDto responseDto = UpdateFilmResponseDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .ratingMpaId(new RatingMpaId(film.getRatingMPA().getId()))
                .releaseDate(film.getReleaseDate())
                .genres(genres)
                .build();

        return responseDto;
    }
}
