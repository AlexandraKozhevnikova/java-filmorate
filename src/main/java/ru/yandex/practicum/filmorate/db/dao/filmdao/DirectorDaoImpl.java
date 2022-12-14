package ru.yandex.practicum.filmorate.db.dao.filmdao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    /*
     Returns new director Id which be generated by database
     */
    public int insertDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");
        return simpleJdbcInsert.executeAndReturnKey(mapDirectorToMap(director)).intValue();
    }

    @Override
    public void update(Director director) {
        String sql = "UPDATE director SET name = ? WHERE director_id = ?";
        jdbcTemplate.update(
                sql,
                director.getName(),
                director.getId()
        );
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM director WHERE director_id = ?", id);
    }

    @Override
    public List<Director> getAllDirectors() {
        List<Director> directorList = jdbcTemplate.query(
                "SELECT director_id, name FROM director",
                this::mapRowToDirector
        );
        return directorList;
    }

    @Override
    public Optional<Director> getDirectorById(int id) {
        SqlRowSet directorRow = jdbcTemplate.queryForRowSet(
                " SELECT director_id, name  FROM director WHERE director_id = ?",
                id
        );

        if (directorRow.next()) {
            Director director = Director.builder()
                    .id(directorRow.getInt("director_id"))
                    .name(directorRow.getString("name"))
                    .build();
            return Optional.of(director);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void upsertFilmDirector(int filmId, List<Director> directors) {
        String sqlDelete = "DELETE FROM film_director WHERE film_id = ?";
        jdbcTemplate.update(sqlDelete, filmId);
        if (!directors.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO film_director (film_id, director_id) VALUES (?,?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, filmId);
                            ps.setInt(2, directors.get(i).getId());
                        }

                        public int getBatchSize() {
                            return directors.size();
                        }
                    });
        }
    }

    @Override
    public List<Director> getFilmDirector(int filmId) {
        String sql = "SELECT fd.director_id, d.name " +
                "FROM film_director fd " +
                "LEFT JOIN director d ON fd.director_id = d.director_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToDirector, filmId);
    }

    @Override
    public boolean isDirectorExist(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT director_id FROM director WHERE director_id = ?", id);
        return rs.next();
    }

    @Override
    public List<Integer> getFilmByDirectorName(String name) {
        String sql = "SELECT film_id " +
                " FROM film_director f " +
                " JOIN director d ON f.director_id = d.director_id " +
                " WHERE d.name ILIKE ? ";

        return jdbcTemplate.queryForList(sql, Integer.class, "%" + name + "%");
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getInt("director_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Map<String, Object> mapDirectorToMap(Director director) {
        Map<String, Object> map = new HashMap<>();
        map.put("director_id", director.getId());
        map.put("name", director.getName());
        return map;
    }
}