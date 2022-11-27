package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.FilmDao;
import ru.yandex.practicum.filmorate.db.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.db.dao.FilmLikeDao;
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

    @Autowired
    public DbFilmStorage(FilmDao filmDao, FilmGenreDao filmGenreDao, FilmLikeDao filmLikeDao) {
        this.filmDao = filmDao;
        this.filmGenreDao = filmGenreDao;
        this.filmLikeDao = filmLikeDao;
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
    public List<Integer> getFilmGenresId(int filmId) {
        return filmGenreDao.getFilmGenres(filmId);
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

        int dif = threshold - filmLikes.size()  ;
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
}
