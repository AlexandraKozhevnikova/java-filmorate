package ru.yandex.practicum.filmorate.db.dao;

import ru.yandex.practicum.filmorate.model.Feed;

public interface FeedDao {

    Feed getFeedById(int id);

    void addFeed(Feed feed);

}
