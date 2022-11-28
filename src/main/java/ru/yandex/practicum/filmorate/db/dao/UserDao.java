package ru.yandex.practicum.filmorate.db.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(int id);

    List<User> getAllUsers();

    int add(User user);

    void update(User user);
}
