package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.FilmDao;
import ru.yandex.practicum.filmorate.db.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.db.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

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
        List<Film> films = filmDao.getAllFilms();
        for (Film film : films) {
            setFieldsOnFilm(film);
        }
        return films;
    }

    @Override
    public Film getItemById(int id) {
        Optional<Film> film = filmDao.getFilmById(id);
        Film filmFromDb = film.orElseThrow(() -> new NoSuchElementException("film with id = " + id + " not found"));
        setFieldsOnFilm(filmFromDb);
        return filmFromDb;
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
    public List<Film> getTopFilms(int threshold, Integer genreId, String year) {
        //  получаем фильмы с лайками
        List<Map<String, Object>> mapLikes = filmLikeDao.getTopLikes(threshold, genreId, year);
        List<Integer> filmIdWithLikes = new ArrayList<>();
        for (Map<String, Object> map : mapLikes) {
            filmIdWithLikes.add((Integer) map.get("film_id"));
        }
        //добиваем фильмы без лайков если лайканных не хватает
        int dif = threshold - filmIdWithLikes.size();
        List<Integer> randomFilmsIdWithoutLike = Collections.emptyList();
        if (dif > 0) {
            randomFilmsIdWithoutLike = filmDao.getFilteredFilm(dif, filmIdWithLikes, genreId, year);
        }
        //собираем в общую коллекцию
        List<Film> filmWithLike = filmIdWithLikes.stream()
                .map(this::getItemById)
                .collect(Collectors.toList());
        randomFilmsIdWithoutLike.stream()
                .map(this::getItemById)
                .forEach(film -> filmWithLike.add(film));

        return filmWithLike;
    }

    @Override
    public boolean isExist(int id) {
        if (!filmDao.isExist(id)) {
            throw new BadFoundResultByIdException("Film with id = " + id + " does not exist");
        }
        return true;
    }

    private void setFieldsOnFilm(Film film) {
        setGenresOnFilm(film);
        //todo add more fields
    }

    private void setGenresOnFilm(Film film) {
        List<Integer> genres = getFilmGenresId(film.getId());
        film.setGenres(genres);
    }
}
