package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class FilmDaoImpL implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedDb;

    public FilmDaoImpL(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    /*
     Returns new film Id which be generated by database
     */
    public int insertFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(mapFilmToMap(film)).intValue();
    }

    @Override
    public boolean isExist(int id) {
        String sql = "SELECT id FROM film WHERE id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT id FROM film WHERE id = ?", id);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_mpa = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
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
        List<Film> filmList = jdbcTemplate.query(
                "SELECT id, name, description, release_date, duration, rating_mpa  FROM film",
                this::mapRowToFilm
        );
        return filmList;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sql = "SELECT id, name, description, release_date, duration, rating_mpa FROM film WHERE id = ?";
        Optional<Film> film = Optional.empty();
        try {
            film = Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException ignored) {
        }
        return film;
    }

    public List<Film> getFilteredFilm(int count, List<Integer> excludeList) {
        namedDb = new NamedParameterJdbcTemplate(jdbcTemplate);

        MapSqlParameterSource parameters = new MapSqlParameterSource("ids", excludeList);
        parameters.addValue("count", count);

        String sql = "SELECT id, name, description, release_date, duration, rating_mpa " +
                "FROM film " +
                (!excludeList.isEmpty() ? "WHERE id NOT IN (:ids) " : "") +
                "LIMIT (:count)";

        List<Film> films = namedDb.query(sql, parameters, this::mapRowToFilm);

        return films;
    }


    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Optional<Date> date = Optional.ofNullable(resultSet.getDate("release_date"));

        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(date
                        .map(Date::toLocalDate)
                        .orElse(null))
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

    public List<Film> getAllFilmsByDirector(int directorId) {
        List<Film> filmList = jdbcTemplate.query(
                        "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_mpa " +
                        "FROM film_director fd " +
                        "LEFT JOIN film f on f.id = fd.film_id " +
                        "LEFT JOIN (" +
                                    "SELECT DISTINCT film_id, COUNT(user_id) AS likecount " +
                                    "FROM film_like " +
                                    "GROUP BY film_id) AS liketemp on fd.film_id = liketemp.film_id " +
                        "WHERE director_id = ? " +
                        "ORDER BY liketemp.likecount DESC",
                this::mapRowToFilm, directorId
        );
        return filmList;
    }
}

