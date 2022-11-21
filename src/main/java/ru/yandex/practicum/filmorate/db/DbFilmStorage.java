package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.FilmDao;
import ru.yandex.practicum.filmorate.db.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("dbFilmStorage")
public class DbFilmStorage implements FilmStorage {

    private final FilmDao filmDao;
    private final FilmGenreDao filmGenreDao;

    @Autowired
    public DbFilmStorage(FilmDao filmDao, FilmGenreDao filmGenreDao) {
        this.filmDao = filmDao;
        this.filmGenreDao = filmGenreDao;
    }

    @Override
    public int add(Film film) {
        int id = filmDao.insertFilm(film);
        filmGenreDao.upsertFilmGenres(id, film.getGenres());
        return id;
    }

    @Override
    public void update(Film film) {
        filmDao.update(film);
        filmGenreDao.upsertFilmGenres(film.getId(), film.getGenres());
    }

    @Override
    public List<Film> getAllItems() {
        return filmDao.getAllFilms();
    }

    @Override
    public Optional<Film> getItemById(int id) {
        return filmDao.getFilmById(id);
    }

    @Override
    public List<Integer> getFilmGenres(int filmId) {
        return filmGenreDao.getFilmGenres(filmId);
    }
}
