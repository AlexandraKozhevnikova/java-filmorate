package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.DirectorDao;
import ru.yandex.practicum.filmorate.db.dao.FilmDao;
import ru.yandex.practicum.filmorate.db.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.db.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.web.dto.SortTypeDirectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Qualifier("dbFilmStorage")
public class DbFilmStorage implements FilmStorage {
    private final FilmDao filmDao;
    private final FilmGenreDao filmGenreDao;
    private final FilmLikeDao filmLikeDao;
    private final DirectorDao directorDao;

    @Autowired
    public DbFilmStorage(FilmDao filmDao, FilmGenreDao filmGenreDao, FilmLikeDao filmLikeDao, DirectorDao directorDao) {
        this.filmDao = filmDao;
        this.filmGenreDao = filmGenreDao;
        this.filmLikeDao = filmLikeDao;
        this.directorDao = directorDao;
    }

    @Override
    public int add(Film film) {
        return filmDao.insertFilm(film);
    }

    @Override
    public void upsertGenresForFilm(int filmId, List<Integer> genres) {
        filmGenreDao.upsertFilmGenres(filmId, genres);
    }

    @Override
    public void upsertDirectorForFilm(int filmId, List<Director> directors) {
        directorDao.upsertFilmDirector(filmId, directors);
    }

    @Override
    public void update(Film film) {
        filmDao.update(film);
        filmGenreDao.upsertFilmGenres(film.getId(), film.getGenres());
        directorDao.upsertFilmDirector(film.getId(), film.getDirector());
    }

    @Override
    public List<Film> getAllItems() {
        List<Film> films = filmDao.getAllFilms();
        for (Film film : films) {
            setFieldsOnFilm(film);
        }
        return films;
    }

    @Override
    public Film getItemById(int id) {
        Optional<Film> film = filmDao.getFilmById(id);
        Film filmFromDb = film.orElseThrow(
                () -> new NoSuchElementException("film with id = " + id + " not found"));
        setFieldsOnFilm(filmFromDb);
        return filmFromDb;
    }

    @Override
    public List<Integer> getFilmGenresId(int filmId) {
        return filmGenreDao.getFilmGenres(filmId);
    }

    @Override
    public List<Director> getFilmDirector(int filmId) {
        return directorDao.getFilmDirector(filmId);
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        filmLikeDao.likeFilm(filmId, userId);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        filmLikeDao.unlikeFilm(filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(int threshold) {
        List<Map<String, Object>> mapLikes = filmLikeDao.getTopLikes(threshold);

        List<Integer> filmLikes = new ArrayList<>();

        for (Map<String, Object> map : mapLikes) {
            filmLikes.add((Integer) map.get("film_id"));
        }

        int dif = threshold - filmLikes.size();
        List<Film> randomFilmsWithoutLike = Collections.emptyList();
        if (dif > 0) {
            randomFilmsWithoutLike = filmDao.getFilteredFilm(dif, filmLikes);
        }

        List<Film> filmWithLike = filmLikes.stream()
                .map(it -> getItemById(it))
                .collect(Collectors.toList());

        filmWithLike.addAll(randomFilmsWithoutLike);
        return filmWithLike;
    }

    @Override
    public boolean isExist(int id) {
        if (!filmDao.isExist(id)) {
            throw new BadFoundResultByIdException("Film with id = " + id + " does not exist");
        }
        return true;
    }

    @Override
    public int addDirector(Director director) {
        return directorDao.insertDirector(director);
    }

    @Override
    public Director getDirectorById(int directorId) {
        Optional<Director> director = directorDao.getDirectorById(directorId);
        return director.orElseThrow(
                () -> new NoSuchElementException("director with id = " + directorId + " not found"));
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorDao.getAllDirectors();
    }

    @Override
    public void updateDirector(Director director) {
        directorDao.update(director);
    }

    @Override
    public void deleteDirector(int directorId) {
        directorDao.delete(directorId);
    }

    @Override
    public List<Film> getAllFilmsByDirector(int directorId, SortTypeDirectors sortTypeForDirectors) {
        List<Film> films = filmDao.getAllFilmsByDirector(directorId);
        for (Film film : films) {
            setFieldsOnFilm(film);
        }
        if (sortTypeForDirectors.equals(SortTypeDirectors.YEAR)) {
          films = films.stream()
                  .sorted(Film::compareFilmsByYear)
                  .collect(Collectors.toList());
        }
        return films;
    }

    @Override
    public boolean isDirectorExist(int directorId) {
        if (!directorDao.isDirectorExist(directorId)) {
            throw new BadFoundResultByIdException("Director with id = " + directorId + " does not exist");
        }
        return true;
    }

    private void setFieldsOnFilm(Film film) {
        setGenresOnFilm(film);
        setDirectorsOnFilm(film);
        //add more fields
    }

    private void setGenresOnFilm(Film film) {
        List<Integer> genres = getFilmGenresId(film.getId());
        film.setGenres(genres);
    }

    private void setDirectorsOnFilm(Film film) {
        List<Director> directors = getFilmDirector(film.getId());
        film.setDirectors(directors);
    }
}
