package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class FilmLikeDaoImpl implements FilmLikeDao {

    private final JdbcTemplate db;

    @Autowired
    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        db = jdbcTemplate;
    }

    //обработать дублирование DuplicateKeyException
    @Override
    public void likeFilm(int filmId, int userId) {
        String sql = "INSERT INTO film_like (film_id, user_id) VALUES (?,?)";
//        try {
            db.update(sql, filmId, userId);
//        } catch (DuplicateKeyException e) {
//            log.warn("Лайк для фильма " + filmId+ " от юзера "+ userId+"уже существует в бд" );
//            log.warn(e.getMessage());
//        }
    }
}
