package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    int add(User item);

    void update(User newItem);

    List<User> getAllItems();

    Optional<User> getItemById(int id);

    void addFriend(int firstFriend, int secondFriend);

    void deleteFriend(int firstFriend, int secondFriend);

    List<Integer> getUserFriends(int userId);
}
