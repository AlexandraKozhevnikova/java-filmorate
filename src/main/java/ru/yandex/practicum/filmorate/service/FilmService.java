package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.web.dto.SearchByType;
import ru.yandex.practicum.filmorate.web.dto.SortTypeDirectors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(
            @Qualifier("dbFilmStorage") FilmStorage filmStorage,
            UserService userService
    ) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllItems();
        return films;
    }

    public Film addFilm(Film filmWithoutId) {
        int id = filmStorage.add(filmWithoutId);
        filmStorage.upsertGenresForFilm(id, filmWithoutId.getGenres());
        filmStorage.upsertDirectorForFilm(id, filmWithoutId.getDirector());
        return getFilmById(id);
    }

    public Film update(Film newFilm) {
        filmStorage.isExist(newFilm.getId());
        log.info("Film with id " + newFilm.getId() + " has found");
        filmStorage.update(newFilm);
        Film newFilmFromDb = getFilmById(newFilm.getId());
        return newFilmFromDb;
    }

    public Film getFilmById(int id) {
        return filmStorage.getItemById(id);
    }

    public String like(int filmId, int userId) {
        filmStorage.isExist(filmId);
        userService.getUserById(userId);
        filmStorage.likeFilm(filmId, userId);
        return "success";
    }

    public String unlike(int filmId, int userId) {
        filmStorage.isExist(filmId);
        userService.getUserById(userId);
        filmStorage.unlikeFilm(filmId, userId);
        return "success";

    }

    public List<Film> getTopFilms(int threshold, Integer genreId, String year) {
        List<Film> films = filmStorage.getTopFilms(threshold, genreId, year);
        return films;
    }

    public List<Genre> getAllGenres() {
        return List.of(Genre.values());
    }

    public Genre getGenreById(int id) {
        return Genre.getGenreById(id).orElseThrow(
                () -> new BadFoundResultByIdException("Genre with id = " + id + " does not exist"));
    }

    public List<RatingMpa> getAllMpa() {
        return List.of(RatingMpa.values());
    }

    public RatingMpa getMpaById(int id) {
        return RatingMpa.getRatingMpaById(id).orElseThrow(
                () -> new BadFoundResultByIdException("RatingMPA with id = " + id + " does not exist"));
    }

    public Director addDirector(Director directorWithoutId) {
        int directorId = filmStorage.addDirector(directorWithoutId);
        return getDirectorById(directorId);
    }

    public Director getDirectorById(int directorId) {
        return filmStorage.getDirectorById(directorId);
    }

    public List<Director> getAllDirectors() {
        return filmStorage.getAllDirectors();
    }

    public Director updateDirector(Director newDirector) {
        filmStorage.isDirectorExist(newDirector.getId());
        log.info("Director with id " + newDirector.getId() + " has found");
        filmStorage.updateDirector(newDirector);
        return getDirectorById(newDirector.getId());
    }

    public void deleteDirector(int id) {
        filmStorage.isDirectorExist(id);
        filmStorage.deleteDirector(id);
        log.info("Director with id " + id + " deleted");
    }

    public List<Film> getAllFilmsByDirector(int directorId, SortTypeDirectors sortTypeForDirector) {
        filmStorage.isDirectorExist(directorId);
        return filmStorage.getAllFilmsByDirector(directorId, sortTypeForDirector);
    }

    // поиск по названию фильма и имени режиссера отсортированный по популярности
    public List<Film> search(String query, List<SearchByType> searchBy) {
        List<Integer> filmWithQuery = new ArrayList<>();

        if (searchBy.contains(SearchByType.NAME)) {
            filmWithQuery.addAll(filmStorage.searchByFilmTitle(query));
        }

        if (searchBy.contains(SearchByType.DIRECTOR)) {
            filmWithQuery.addAll(filmStorage.searchByFilmDirector(query));
        }

        if (!filmWithQuery.isEmpty()) {
            filmWithQuery = filmStorage.sortByPopular(filmWithQuery);
        }

        return filmWithQuery.stream()
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }
}
