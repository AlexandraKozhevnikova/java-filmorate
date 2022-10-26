package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage{

    User add(User item);

    User update(User newItem);

    List<User> getAllItems();

    User getItemById(int id);
}
