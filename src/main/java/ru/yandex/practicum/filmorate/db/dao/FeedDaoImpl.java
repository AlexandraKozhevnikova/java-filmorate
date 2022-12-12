package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.web.mapper.FeedMapper;

import java.sql.PreparedStatement;
import java.util.List;

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
            stmt.setString(3, feed.getEventType());
            stmt.setString(4, feed.getOperation());
            stmt.setLong(5, feed.getTimestamp());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        feed.setEventId(id);
    }

    @Override
    public List<Feed> getFeedById(int id) {
        String sqlQuery = "SELECT * from feed where userID=?";

        List<Feed> feed = jdbcTemplate.query(sqlQuery, new Object[]{id},
                new FeedMapper());
        return feed;

    }
}
