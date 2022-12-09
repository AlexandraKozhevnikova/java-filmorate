package ru.yandex.practicum.filmorate.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.web.dto.SortTypeDirectors;

import java.util.List;

@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {

    @Override
    public void upsertGenresForFilm(int filmId, List<Integer> genres) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Integer> getFilmGenresId(int filmId) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Film> getTopFilms(int threshold, Integer genreId, String year) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Integer> searchByFilmTitle(String query) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Integer> searchByFilmDirector(String query) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Integer> sortByPopular(List<Integer> filmWithQuery) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public boolean isExist(int id) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public int addDirector(Director director) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public Director getDirectorById(int id) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Director> getAllDirectors() {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public void updateDirector(Director director) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public void deleteDirector(int id) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public void upsertDirectorForFilm(int filmId, List<Director> directors) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Director> getFilmDirector(int filmId) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Film> getAllFilmsByDirector(int directorId, SortTypeDirectors sortTypeForDirectors) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public boolean isDirectorExist(int id) {
        throw new NotImplementedException("метод реализован только для БД");
    }
}
