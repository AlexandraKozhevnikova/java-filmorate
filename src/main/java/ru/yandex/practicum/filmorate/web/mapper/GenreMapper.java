package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.genre.GenreDto;

import java.util.ArrayList;
import java.util.List;

public class GenreMapper {

    public static GenreDto mapGenreToGenreDto(Genre genre) {
        GenreDto dto = GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        return dto;
    }


}
