package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.web.mapper.FeedMapper;

import java.sql.Date;
import java.sql.PreparedStatement;

@Component
public class FeedDaoImpl implements FeedDao {

    private final JdbcTemplate jdbcTemplate;

    public FeedDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFeed(Feed feed) {
        String sqlQuery = "insert into feed(entityId, userId, eventType, operation, eventTime) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"eventId"});
            stmt.setInt(1, feed.getEntityId());
            stmt.setInt(2, feed.getUserId());
            stmt.setInt(3, feed.getEventType().ordinal());
            stmt.setInt(4, feed.getOperation().ordinal());
            stmt.setTimestamp(5, feed.getEventTime());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        feed.setEventId(id);
    }

    @Override
    public Feed getFeedById(int id) {
        String sqlQuery = "SELECT * from feed where eventId=?";

        Feed feed = jdbcTemplate.query(sqlQuery, new Object[]{id},
                new FeedMapper()).stream().findAny().orElse(null);
        return feed;

    }
}
