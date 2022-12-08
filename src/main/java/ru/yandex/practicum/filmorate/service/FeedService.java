package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dao.FeedDao;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

@Service
public class FeedService {

    @Autowired
    private final FeedDao feedDao;

    public FeedService(FeedDao feedDao) {
        this.feedDao = feedDao;
    }

    public List<Feed> getFeedById(int id) {
        return feedDao.getFeedById(id);
    }

}
