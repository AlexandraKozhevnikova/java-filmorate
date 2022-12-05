package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.db.dao.UserDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@Qualifier("dbUserStorage")
public class DbUserStorage implements UserStorage {

    private final UserDao userDao;
    private final FriendshipDao friendshipDao;

    @Autowired
    public DbUserStorage(UserDao userDao, FriendshipDao friendshipDao) {
        this.userDao = userDao;
        this.friendshipDao = friendshipDao;
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
    public User getItemById(int id) {
        Optional<User> user = userDao.getUserById(id);
        return user.orElseThrow(
                () -> new NoSuchElementException("user with id = " + id + " not found")
        );
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
}
