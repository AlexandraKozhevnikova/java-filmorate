package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        films.forEach(film -> film.setGenres(
                filmStorage.getFilmGenresId(film.getId())));
        return films;
    }

    public Film addFilm(Film filmWithoutId) {
        int id = filmStorage.add(filmWithoutId);
        Film filmFromDbWithoutGenres = getFilmById(id);
        filmStorage.upsertGenresForFilm(filmFromDbWithoutGenres.getId(), filmWithoutId.getGenres());
        List<Integer> genres = filmStorage.getFilmGenresId(filmFromDbWithoutGenres.getId());
        filmFromDbWithoutGenres.setGenres(genres);
        return filmFromDbWithoutGenres;
    }

    public Film update(Film newFilm) {
        Film newFilmFromDb;
        Optional<Film> oldFilm = filmStorage.getItemById(newFilm.getId());

        if (oldFilm.isPresent()) {
            log.info("Film with id " + newFilm.getId() + " has found");
            filmStorage.update(newFilm);
            newFilmFromDb = getFilmById(newFilm.getId());
            List<Integer> genres = filmStorage.getFilmGenresId(newFilmFromDb.getId());
            newFilmFromDb.setGenres(genres);
        } else {
            log.warn("Film can not be updated cause user with id = " + newFilm.getId() + " not found");
            throw new NoSuchElementException("Film can not be updated cause user with id = " + newFilm.getId() + " not found");
        }
        return newFilmFromDb;
    }

    public Film getFilmById(int id) {
        Optional<Film> film = filmStorage.getItemById(id);
        Film filmFromDb = film.orElseThrow(
                () -> new NoSuchElementException("film with id = " + id + " not found"));
        List<Integer> genres = filmStorage.getFilmGenresId(filmFromDb.getId());
        filmFromDb.setGenres(genres);
        return filmFromDb;
    }

    public String like(int filmId, int userId) {
        getFilmById(filmId);
        userService.getUserById(userId);
        filmStorage.likeFilm(filmId, userId);
        return "success";
    }

    public String unlike(int filmId, int userId) {
        getFilmById(filmId);
        userService.getUserById(userId);
        filmStorage.unlikeFilm(filmId, userId);
        return "success";

    }

    public List<Film> getTopFilms(int threshold, Integer genreId, String year) {
        if (genreId != null) {getGenreById(genreId);}

        List<Film> films = filmStorage.getTopFilms(threshold, genreId, year);
        films.forEach(film -> film.setGenres(
                filmStorage.getFilmGenresId(film.getId())));
        // todo добавить добивку режиссерами
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
}
