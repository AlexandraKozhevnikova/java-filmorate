package ru.yandex.practicum.filmorate.db.dao.filmdao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FilmLikeDaoImpl implements FilmLikeDao {

    //интовое значение которое точно не может быть равно валидному идетификатору жанра
    private static final int GENRE_STUB = -1;
    //стринговое значение которое точно не может быть равно валидному году выпуска фильма
    private static final String YEAR_STUB = "0000";

    private final JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedDb;


    @Autowired
    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void likeFilm(int filmId, int userId) throws DuplicateKeyException {
        String sql = "INSERT INTO film_like (film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        String sql = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Map<String, Object>> getTopLikes(int threshold, Integer genreId, String year) {
        if (year == null) {
            year = YEAR_STUB;
        }

        if (genreId == null) {
            genreId = GENRE_STUB;
        }

        String sql = "WITH " +
                "genre_filtred(film_id) AS (" +
                "   SELECT film_id " +
                "   FROM film_genre " +
                "   WHERE 1 = 1 " +
                //проверяем нужна ли фильтрация по жанру
                // Плейсхолдер ? нужно чтобы был использован не зависимо от результата проверки на условие
                (genreId == GENRE_STUB ? " AND 1 <> ?" : " AND genre_id = ?") +
                "), " +
                "film_filtred(film_id) AS (" +
                "   SELECT id " +
                "   FROM film " +
                "   WHERE 1=1 " +
                //проверяем нужна ли фильтрация по году
                (year == YEAR_STUB ? " AND 1 <> ? " : "AND  EXTRACT(YEAR FROM release_date) = ?") +
                ") " +
                "SELECT film_like.film_id, COUNT(user_id) as count_likes " +
                "FROM film_like " +
                //проверяем нужно ли сохранить в результате джойна отфильтрованные по жанру фильмы
                // (в случае если эти фильмы окажутся без лайка и количества залайканных фильмов не хватит
                // для  колисчества threshold)
                (genreId == GENRE_STUB ? " LEFT " : " ") +
                "    JOIN genre_filtred ON FILM_LIKE.FILM_ID = genre_filtred.film_id " +
                //проверяем нужно ли сохранить в результате джойна отфильтрованные по году фильмы
                (year == YEAR_STUB ? " LEFT " : " ") +
                "    JOIN film_filtred ON FILM_LIKE.FILM_ID = film_filtred.film_id " +
                "GROUP BY FILM_LIKE.film_id " +
                "ORDER BY count_likes desc " +
                "LIMIT ? ";

        return jdbcTemplate.queryForList(sql, genreId, year, threshold);
    }

    @Override
    public List<Integer> sortByPopular(List<Integer> filmWithQuery) {
        if (filmWithQuery.isEmpty()) {
            return Collections.emptyList();
        }
        namedDb = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource parameters = new MapSqlParameterSource(Map.of("ids", filmWithQuery));

        String sql =
                " SELECT film_found.id " +
                        " FROM (VALUES :ids ) AS film_found(ID)  " +
                        "       LEFT  JOIN film_like fl ON film_found.id = fl.film_id " +
                        " GROUP BY film_found.id " +
                        " ORDER BY COUNT(fl.user_id) DESC ";

        return namedDb.queryForList(sql, parameters, Integer.class);
    }
}