package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.RecommendationsDao;
import ru.yandex.practicum.filmorate.db.dao.filmdao.DirectorDao;
import ru.yandex.practicum.filmorate.db.dao.filmdao.FilmDao;
import ru.yandex.practicum.filmorate.db.dao.filmdao.FilmGenreDao;
import ru.yandex.practicum.filmorate.db.dao.filmdao.FilmLikeDao;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.web.dto.SortTypeDirectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@Qualifier("dbFilmStorage")
public class DbFilmStorage implements FilmStorage {
    private final FilmDao filmDao;
    private final FilmGenreDao filmGenreDao;
    private final FilmLikeDao filmLikeDao;
    private final DirectorDao directorDao;
    private final RecommendationsDao recommendationsDao;

    @Autowired
    public DbFilmStorage(FilmDao filmDao,
                         FilmGenreDao filmGenreDao,
                         FilmLikeDao filmLikeDao,
                         DirectorDao directorDao,
                         RecommendationsDao recommendationsDao) {
        this.filmDao = filmDao;
        this.filmGenreDao = filmGenreDao;
        this.filmLikeDao = filmLikeDao;
        this.directorDao = directorDao;
        this.recommendationsDao = recommendationsDao;
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
        Film filmFromDb = filmDao.getFilmById(id).orElseThrow(() -> new NoSuchElementException("film with id = " + id + " not found"));
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
    public List<Film> getTopFilms(int threshold, Integer genreId, String year) {
        //  получаем фильмы с лайками
        List<Map<String, Object>> mapLikes = filmLikeDao.getTopLikes(threshold, genreId, year);
        List<Integer> filmIdWithLikes = new ArrayList<>();
        for (Map<String, Object> map : mapLikes) {
            filmIdWithLikes.add((Integer) map.get("film_id"));
        }
        //добываем фильмы без лайков если лайканных не хватает
        int dif = threshold - filmIdWithLikes.size();
        List<Integer> randomFilmsIdWithoutLike = Collections.emptyList();
        if (dif > 0) {
            randomFilmsIdWithoutLike = filmDao.getFilteredFilm(dif, filmIdWithLikes, genreId, year, null);
        }
        List<Film> filmWithLike = filmIdWithLikes.stream()
                .map(this::getItemById)
                .collect(Collectors.toList());

        //собираем в общую коллекцию
        randomFilmsIdWithoutLike.stream()
                .map(this::getItemById)
                .forEach(film -> filmWithLike.add(film));

        return filmWithLike;
    }

    @Override
    public List<Integer> searchByFilmTitle(String query) {
        List<Integer> films = filmDao.getFilteredFilm(0, Collections.emptyList(), null, null, query);
        return films;
    }

    @Override
    public List<Integer> searchByFilmDirector(String name) {
        return directorDao.getFilmByDirectorName(name);
    }

    @Override
    public List<Integer> sortByPopular(List<Integer> filmWithQuery) {
        return filmLikeDao.sortByPopular(filmWithQuery);
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
        return directorDao.getDirectorById(directorId).orElseThrow(
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
        if (sortTypeForDirectors == SortTypeDirectors.YEAR) {
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

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        List<Integer> commonFilmIds = recommendationsDao.getCommonFilmsIds(userId, friendId);
        List<Integer> sortedCommonFilmIds = sortByPopular(commonFilmIds);
        return sortedCommonFilmIds.stream()
                .map(this::getItemById)
                .collect(Collectors.toList());
    }

    private void setFieldsOnFilm(Film film) {
        setGenresOnFilm(film);
        setDirectorsOnFilm(film);
    }

    private void setGenresOnFilm(Film film) {
        List<Integer> genres = getFilmGenresId(film.getId());
        film.setGenres(genres);
    }

    @Override
    public void deleteFilm(int filmId) {
        filmDao.deleteFilm(filmId);
    }


    private void setDirectorsOnFilm(Film film) {
        List<Director> directors = getFilmDirector(film.getId());
        film.setDirectors(directors);
    }
}
