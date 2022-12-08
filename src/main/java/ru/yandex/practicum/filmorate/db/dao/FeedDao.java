package ru.yandex.practicum.filmorate.db.dao;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedDao {

    List<Feed> getFeedById(int id);

    void addFeed(Feed feed);

}
