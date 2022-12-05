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
        films.forEach(film -> film.setDirector(
                filmStorage.getFilmDirector(film.getId())));
        return films;
    }

    public Film addFilm(Film filmWithoutId) {
        int id = filmStorage.add(filmWithoutId);
        Film filmFromDbWithoutGenres = getFilmById(id);
        filmStorage.upsertGenresForFilm(filmFromDbWithoutGenres.getId(), filmWithoutId.getGenres());
        List<Integer> genres = filmStorage.getFilmGenresId(filmFromDbWithoutGenres.getId());
        filmFromDbWithoutGenres.setGenres(genres);

        filmStorage.upsertDirectorForFilm(filmFromDbWithoutGenres.getId(), filmWithoutId.getDirector());
        List<Director> directors = filmStorage.getFilmDirector(filmFromDbWithoutGenres.getId());
        filmFromDbWithoutGenres.setDirector(directors);

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
            List<Director> directors = filmStorage.getFilmDirector(newFilmFromDb.getId());
            newFilmFromDb.setDirector(directors);
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
        List<Director> directors = filmStorage.getFilmDirector(filmFromDb.getId());
        filmFromDb.setDirector(directors);
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

    public List<Film> getTopFilms(int threshold) {
        List<Film> films = filmStorage.getTopFilms(threshold);
        films.forEach(film -> film.setGenres(
                filmStorage.getFilmGenresId(film.getId())));
        films.forEach(film -> film.setDirector(
                filmStorage.getFilmDirector(film.getId())));
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
        int id = filmStorage.addDirector(directorWithoutId);
        return getDirectorById(id);
    }

    public Director getDirectorById(int id) {
        Optional<Director> director = filmStorage.getDirectorById(id);
        return director.orElseThrow(
                () -> new NoSuchElementException("director with id = " + id + " not found"));
    }

    public List<Director> getAllDirectors() {
        return filmStorage.getAllDirectors();
    }

    public Director updateDirector(Director newDirector) {
        Director newDirectorFromDb;
        Optional<Director> oldDirector = filmStorage.getDirectorById(newDirector.getId());

        if (oldDirector.isPresent()) {
            log.info("Director with id " + newDirector.getId() + " has found");
            filmStorage.updateDirector(newDirector);
            newDirectorFromDb = getDirectorById(newDirector.getId());
        } else {
            log.warn("Director can not be updated cause director with id = " + newDirector.getId() + " not found");
            throw new NoSuchElementException("Director can not be updated cause director with id = "
                    + newDirector.getId() + " not found");
        }
        return newDirectorFromDb;
    }

    public void deleteDirector(int id) {
        Optional<Director> director = filmStorage.getDirectorById(id);

        if (director.isPresent()) {
            filmStorage.deleteDirector(id);
            log.info("Director with id " + id + " deleted");
        } else {
            log.warn("Director can not be deleted cause director with id = " + id + " not found");
            throw new NoSuchElementException("Director can not be deleted cause director with id = "
                    + id + " not found");
        }
    }

    public List<Film> getAllFilmsByDirector(int directorId, String sortTypeForDirector) {
        Optional<Director> director = filmStorage.getDirectorById(directorId);

        List<String> sortCodes = List.of("year", "likes");
        if (!sortCodes.contains(sortTypeForDirector)) {
            log.warn("Wrong type of sort declared");
            throw new NoSuchElementException("Wrong type of sort declared");
        }

        if (director.isPresent()) {
            List<Film> films = filmStorage.getAllFilmsByDirector(directorId, sortTypeForDirector);
            films.forEach(film -> film.setGenres(
                    filmStorage.getFilmGenresId(film.getId())));
            films.forEach(film -> film.setDirector(
                    filmStorage.getFilmDirector(film.getId())));
            return films;
        } else {
            log.warn("All films by director can not be shown cause director with id = " + directorId + " not found");
            throw new NoSuchElementException("All films by director can not be shown cause director with id  = "
                    + directorId + " not found");
        }
    }
}
