package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.DirectorDao;
import ru.yandex.practicum.filmorate.db.dao.FilmDao;
import ru.yandex.practicum.filmorate.db.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.db.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        return filmDao.getAllFilms();
    }

    @Override
    public Optional<Film> getItemById(int id) {
        return filmDao.getFilmById(id);
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
                .map(it -> getItemById(it).orElseThrow())
                .collect(Collectors.toList());

        filmWithLike.addAll(randomFilmsWithoutLike);
        return filmWithLike;
    }

    @Override
    public int addDirector(Director director) {
        return directorDao.insertDirector(director);
    }

    @Override
    public Optional<Director> getDirectorById(int id) {
        return directorDao.getDirectorById(id);
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
    public void deleteDirector(int id) {
        directorDao.delete(id);
    }

    @Override
    public List<Film> getAllFilmsByDirector(int directorId, String sortTypeForDirector) {
        return filmDao.getAllFilmsByDirector(directorId, sortTypeForDirector);
    }
}
