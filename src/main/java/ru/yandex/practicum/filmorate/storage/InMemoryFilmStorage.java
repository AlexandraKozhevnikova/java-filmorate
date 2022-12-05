package ru.yandex.practicum.filmorate.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
    public boolean isExist(int id) {
        throw new NotImplementedException("метод реализован только для БД");
    }
}
