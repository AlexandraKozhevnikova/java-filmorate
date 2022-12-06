package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dao.FeedDao;
import ru.yandex.practicum.filmorate.model.Feed;

@Service
public class FeedService {

    @Autowired
    private final FeedDao feedDao;

    public FeedService(FeedDao feedDao) {
        this.feedDao = feedDao;
    }

    public Feed getFeedById(int id) {
        return feedDao.getFeedById(id);
    }

}
