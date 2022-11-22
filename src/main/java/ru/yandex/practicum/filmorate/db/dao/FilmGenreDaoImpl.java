package ru.yandex.practicum.filmorate.db.dao;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final JdbcTemplate db;

    public FilmGenreDaoImpl(JdbcTemplate db) {
        this.db = db;
    }

    @Override
    @Transactional
    public void upsertFilmGenres(int filmId, List<Integer> genres) {
        String sqlDelete = "DELETE FROM film_genre WHERE film_id = ?";
        db.update(sqlDelete, filmId);

        if (!genres.isEmpty()) {
            String sqlInsert = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";
            for (Integer i : genres) {
                try {
                    db.update(sqlInsert, filmId, i);
                } catch (DuplicateKeyException e) {
                    continue;
                }
            }
        }
    }

    @Override
    public List<Integer> getFilmGenres(int filmId) {
        String sql = "SELECT genre_id FROM film_genre WHERE film_id = ?";
        List<Integer> list = new ArrayList<>();
        db.queryForList(sql, filmId).stream()
                .map(map -> map.values())
                .flatMap(object -> object.stream())
                .mapToInt(it -> (int) it)
                .forEach(it -> list.add(it));

        return list;
    }
}
