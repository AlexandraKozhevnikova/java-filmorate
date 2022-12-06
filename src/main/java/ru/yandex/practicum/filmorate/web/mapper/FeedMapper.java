package ru.yandex.practicum.filmorate.web.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FeedMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEventId(rs.getInt("eventId"));
        feed.setEntityId(rs.getInt("entityId"));
        feed.setUserId(rs.getInt("userId"));
        feed.setEventType(EventType.getEventTypeByID(rs.getInt("eventType")));
        feed.setOperation(Operation.getOperationByID(rs.getInt("operation")));
        feed.setEventTime(rs.getTimestamp("eventTime"));
        return feed;
    }
}
