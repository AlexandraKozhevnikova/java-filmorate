package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate db;

    public GenreDaoImpl(JdbcTemplate db) {
        this.db = db;
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT id, name FROM genre";
        List<Genre> list = db.query(sql, this::mapRowToGenre);
        return list;
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        SqlRowSet genreRow = db.queryForRowSet(
                " SELECT id, name  FROM genre WHERE id = ?",
                id
        );

        if (genreRow.next()) {
            Genre genre = Genre.builder()
                    .id(genreRow.getInt("id"))
                    .name(genreRow.getString("name"))
                    .build();
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }


    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
        return genre;
    }
}
