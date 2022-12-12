package ru.yandex.practicum.filmorate.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.annotation.FeedAnnotation;
import ru.yandex.practicum.filmorate.db.dao.FeedDao;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class FeedAspect {

    @Autowired
    private FeedDao feedDao;

    @Around("@annotation(ru.yandex.practicum.filmorate.annotation.FeedAnnotation)")
    public Object addFeed(ProceedingJoinPoint joinPoint) throws Throwable {
        
        final String name = joinPoint.getSignature().getName();
        FeedAnnotation annotation = Arrays.stream(joinPoint.getTarget().getClass().getMethods()).
                filter(method -> method.getName().equals(name)).findFirst().get()
                .getAnnotation(FeedAnnotation.class);
        Operation operation = annotation.operation();
        EventType eventType = annotation.eventType();
        Object[] arguments = joinPoint.getArgs();

        Object result = joinPoint.proceed(arguments);

        if ((operation != Operation.REMOVE) && result != null && result.getClass().equals(Review.class)) {
            arguments = new Object[] {result};
        }

        Feed feed = createFeed(operation, eventType, arguments);
        feedDao.addFeed(feed);

        return result;
    }

    private Feed createFeed(Operation operation, EventType eventType, Object[] arguments) {
        Feed feed = new Feed();
        feed.setOperation(operation.getName());
        feed.setEventType(eventType.getName());
        feed.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime());
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

            Review review = (Review) arguments[0];
            Integer reviewId = review.getId();
            Integer userId = review.getUserId();
            feed.setUserId(userId);
            feed.setEntityId(reviewId);

        }

        return feed;
    }

}
