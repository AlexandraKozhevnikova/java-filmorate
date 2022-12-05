package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    int add(Film film);

    void upsertGenresForFilm(int filmId, List<Integer> genres);

    void update(Film film);

    List<Film> getAllItems();

    Optional<Film> getItemById(int id);

    List<Integer> getFilmGenresId(int filmId);

    void likeFilm(int filmId, int userId);

    void unlikeFilm(int filmId, int userId);

    List<Film> getTopFilms(int threshold);

    public int addDirector(Director director);

    public Optional<Director> getDirectorById(int id);

    public List<Director> getAllDirectors();

    public void updateDirector(Director director);

    public void deleteDirector(int id);

    public void upsertDirectorForFilm(int filmId, List<Director> directors);

    public List<Director> getFilmDirector(int filmId);

    public List<Film> getAllFilmsByDirector(int directorId, String sortTypeForDirector);
}

