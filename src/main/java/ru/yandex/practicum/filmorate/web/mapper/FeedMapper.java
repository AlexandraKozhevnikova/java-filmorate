package ru.yandex.practicum.filmorate.web.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEventId(rs.getInt("eventId"));
        feed.setEntityId(rs.getInt("entityId"));
        feed.setUserId(rs.getInt("userId"));
        feed.setEventType(rs.getString("eventType"));
        feed.setOperation(rs.getString("operation"));
        feed.setTimestamp(rs.getLong("eventTime"));
        return feed;
    }
}
