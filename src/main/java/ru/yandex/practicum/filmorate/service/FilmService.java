package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
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
        return filmStorage.getAllItems();
    }

    public Film addFilm(Film film) {
        int id = filmStorage.add(film);
        Film filmFromDb = getFilmById(id);
        List<Integer> genres = filmStorage.getFilmGenres(filmFromDb.getId());
        filmFromDb.setGenres(genres);
        return filmFromDb;
    }

    public Film update(Film film) {
        filmStorage.update(film);
        Film filmFromDb = getFilmById(film.getId());
        List<Integer> genres = filmStorage.getFilmGenres(filmFromDb.getId());
        filmFromDb.setGenres(genres);
        return filmFromDb;
    }

    public Film getFilmById(int id) {
        Optional<Film> film = filmStorage.getItemById(id);
        return film.orElseThrow(
                () -> new NoSuchElementException("film with id = " + id + " not found")
        );
    }


    public void like(int filmId, int userId) {
        filmStorage.getItemById(filmId);
        userService.getUserById(userId);

    }

    public void unlike(int filmId, int userId) {
        filmStorage.getItemById(filmId);

    }

    public Set<Film> getTopFilms(int threshold) {
        Set<Film> filmList = null;
        return filmList;
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        Optional<Genre> genre = filmStorage.getGenreById(id);
        return genre.orElseThrow(
                () -> new NoSuchElementException("genre with id = " + id + " not found")
        );
    }

//    public List<Integer> getFilmGenres(int filmId) {
//        List<Integer> genres = filmStorage.getFilmGenres(filmId);
//        if (genres.isEmpty()) {
//            throw new NoSuchElementException("genres have not found for film with id = " + filmId);
//        }
//        return genres;
//    }
}
