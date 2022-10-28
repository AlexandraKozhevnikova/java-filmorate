package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.user.AddUserResponseDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class FilmMapper {
    public Film mapToFilm(AddFilmRequestDto dto) {
        Film film = new Film();
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        return film;
    }

    public Film mapToFilm(UpdateFilmRequestDto dto) {
        Film film = new Film();
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setId(dto.getId());
        return film;
    }


}
