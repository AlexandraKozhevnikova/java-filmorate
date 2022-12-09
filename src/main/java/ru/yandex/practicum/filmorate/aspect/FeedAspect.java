package ru.yandex.practicum.filmorate.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.FeedDao;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Aspect
@Component
public class FeedAspect {

    @Autowired
    private FeedDao feedDao;

    @Around("@annotation(ru.yandex.practicum.filmorate.annotation.FeedAnnotation)")
    public void addFeed(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName().toLowerCase();
        Operation operation = getOperation(name);
        EventType eventType = getEventType(name);
        Object[] arguments = joinPoint.getArgs();
        Feed feed = createFeed(operation, eventType, arguments);

        joinPoint.proceed();

        feedDao.addFeed(feed);

    }

    private Feed createFeed(Operation operation, EventType eventType, Object[] arguments) {
        Feed feed = new Feed();
        feed.setOperation(operation.getName());
        feed.setEventType(eventType.getName());
        feed.setEventTime(Timestamp.valueOf(LocalDateTime.now()));
        if (eventType.equals(EventType.FRIEND)) {
            Integer userId = (Integer) arguments[0];
            Integer friendId = (Integer) arguments[1];
            feed.setUserId(userId);
            feed.setEntityId(friendId);
        } else if (eventType.equals(EventType.LIKE)) {
            Integer filmId = (Integer) arguments[0];
            Integer userId = (Integer) arguments[1];
            feed.setUserId(userId);
            feed.setEntityId(filmId);
        } else if (eventType.equals(EventType.REVIEW)) {
            Integer reviewId = (Integer) arguments[0];
            Integer userId = (Integer) arguments[1];
            feed.setUserId(userId);
            feed.setEntityId(reviewId);
        }

        return feed;
    }

    private Operation getOperation(String name) {
        if (name.contains("add") || name.contains("insert")  || name.contains("make") || name.equals("like")) {
            return Operation.ADD;
        } else if (name.contains("remove") || name.contains("delete") || name.equals("unlike")) {
            return Operation.REMOVE;
        } else if (name.equals("update")) {
            return Operation.UPDATE;
        }
        return null;
    }

    private EventType getEventType(String name) {
        if (name.contains("like")) {
            return EventType.LIKE;
        } else if (name.contains("friend")) {
            return EventType.FRIEND;
        } else if (name.equals("review")) {
            return EventType.REVIEW;
        }
        return null;
    }

}
