package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Log4j2
public class FilmDaoImpL implements FilmDao {
    private final JdbcTemplate db;

    private NamedParameterJdbcTemplate namedDb;

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
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_MPA = ? WHERE id = ?";
        db.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingMpaId(),
                film.getId()
        );
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmList = db.query(
                "SELECT id, name, description, release_date, duration, rating_MPA  FROM film",
                this::mapRowToFilm
        );
        return filmList;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sql = "SELECT id, name, description, release_date, duration, rating_MPA FROM film WHERE id = ?";
        Film film;
        try {
            film = db.queryForObject(sql, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            film = null;
        }
        return Optional.ofNullable(film);
    }

    public List<Film> getFilteredFilm(int count, List<Integer> excludeList) {
        namedDb = new NamedParameterJdbcTemplate(db);

        MapSqlParameterSource parameters = new MapSqlParameterSource("ids", excludeList);
        parameters.addValue("count", count);

        String sql = "SELECT id, name, description, release_date, duration, rating_MPA " +
                "FROM film " +
                (!excludeList.isEmpty() ? "WHERE id NOT IN (:ids) " : "") +
                "LIMIT (:count)";

        List<Film> films = namedDb.query(
                sql,
                parameters,
                this::mapRowToFilm
        );

        return films;
    }


    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .ratingMpaId(resultSet.getInt("rating_MPA"))
                .build();
    }

    private Map<String, Object> mapFilmToMap(Film film) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", film.getId());
        map.put("name", film.getName());
        map.put("description", film.getDescription());
        map.put("release_date", film.getReleaseDate());
        map.put("duration", film.getDuration());
        map.put("rating_MPA", film.getRatingMpaId());

        return map;
    }
}

