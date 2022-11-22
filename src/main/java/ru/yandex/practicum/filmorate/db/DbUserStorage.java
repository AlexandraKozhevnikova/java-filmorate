package ru.yandex.practicum.filmorate.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.UserDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.web.dto.user.UserDbEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("dbUserStorage")
public class DbUserStorage implements UserStorage {

    private final UserDao userDao;

    public DbUserStorage(UserDao userDao) {
        this.userDao = userDao;
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
        return Collections.emptyList(); //todo
        //return userDao.getAllUsers();
    }

    @Override
    public Optional<User> getItemById(int id) {
        Optional<User> user = userDao.getUserById(id);
        return user;
    }
}
