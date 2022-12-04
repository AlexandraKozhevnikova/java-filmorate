package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.db.dao.UserDao;
import ru.yandex.practicum.filmorate.db.dao.RecommendationsDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("dbUserStorage")
public class DbUserStorage implements UserStorage {

    private final UserDao userDao;
    private final FriendshipDao friendshipDao;
    private final RecommendationsDao recommendationsDao;

    @Autowired
    public DbUserStorage(UserDao userDao, FriendshipDao friendshipDao, RecommendationsDao recommendationsDao) {
        this.userDao = userDao;
        this.friendshipDao = friendshipDao;
        this.recommendationsDao = recommendationsDao;
    }

    @Override
    public int add(User user) {
        return userDao.add(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public List<User> getAllItems() {
        return userDao.getAllUsers();
    }

    @Override
    public Optional<User> getItemById(int id) {
        Optional<User> user = userDao.getUserById(id);
        return user;
    }

    @Override
    public void addFriend(int firstFriend, int secondFriend) {
        friendshipDao.addFriend(firstFriend, secondFriend);
    }

    @Override
    public void deleteFriend(int firstFriend, int secondFriend) {
        friendshipDao.deleteFriend(firstFriend, secondFriend);
    }

    @Override
    public List<Integer> getUserFriends(int userId) {
         return friendshipDao.getUserFriends(userId);
    }

    @Override
    public List<Film> getRecommendations(int userId) {
        return recommendationsDao.getRecommendations(userId);
    }
}
