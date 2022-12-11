package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.web.dto.director.AddDirectorRequest;
import ru.yandex.practicum.filmorate.web.dto.director.DirectorResponse;
import ru.yandex.practicum.filmorate.web.dto.director.UpdateDirectorRequest;

public class DirectorMapper {
    public static Director mapToDirector(AddDirectorRequest dto) {
        Director director = Director.builder()
                .name(dto.getName())
                .build();
        return director;
    }

    public static Director mapToDirector(UpdateDirectorRequest dto) {
        return Director.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public static DirectorResponse mapDirectorToDirectorResponse(Director director) {
        return DirectorResponse.builder()
                .id(director.getId())
                .name(director.getName())
                .build();
    }

    public static Director mapDirectorForFilm(int directorId) {
        return Director.builder()
                .id(directorId)
                .name("")
                .build();
    }
}
