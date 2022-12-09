package ru.yandex.practicum.filmorate.db.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDao {

    int insertDirector(Director director);

    void update(Director director);

    void delete(int id);

    List<Director> getAllDirectors();

    Optional<Director> getDirectorById(int id);

    void upsertFilmDirector(int filmId, List<Director> directors);

    List<Director> getFilmDirector(int filmId);

    boolean isDirectorExist(int id);

    List<Integer> getFilmByDirectorName(String name);
}


