package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FilmDaoImpL implements FilmDao {
    private final JdbcTemplate db;

    public FilmDaoImpL(JdbcTemplate db) {
        this.db = db;
    }

    @Override
    public int insertFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(db)
                .withTableName("film")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(mapFilmToMap(film)).intValue();
    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, ratingMPA = ? WHERE id = ?";
        db.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingMPA().getId(),
                film.getId()
        );
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmList = db.query(
                "SELECT id, name, description, release_date, duration, ratingMPA  FROM film",
                this::mapRowToFilm
        );
        return filmList;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sql = "SELECT id, name, description, release_date, duration, ratingMPA FROM film WHERE id = ?";
        Film film;
        try {
            film = db.queryForObject(sql, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            film = null;
        }
        return Optional.ofNullable(film);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        RatingMPA ratingMPA;
        try {
            ratingMPA = RatingMPA.getRatingMpaById(resultSet.getInt("ratingMPA"));
        } catch (IllegalArgumentException | NullPointerException e) {
            ratingMPA = null;
        }

        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .ratingMPA(ratingMPA)
                .build();
    }

    private Map<String, Object> mapFilmToMap(Film film) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", film.getId());
        map.put("name", film.getName());
        map.put("description", film.getDescription());
        map.put("release_date", film.getReleaseDate());
        map.put("duration", film.getDuration());
        map.put("ratingMPA", film.getRatingMPA().getId());

        return map;
    }
}

