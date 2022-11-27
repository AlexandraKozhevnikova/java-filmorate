package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        int id = userStorage.add(user);
        return getUserById(id);
    }

    public User update(User user) {
        Optional<User> existUser = userStorage.getItemById(user.getId());
        if (existUser.isPresent()) {
            log.info("User with id " + user.getId() + " has found");
            userStorage.update(user);
        } else {
            log.warn("User can not be updated cause user with id = " + user.getId() + " not found");
            throw new NoSuchElementException("User can not be updated cause user with id = " + user.getId() + " not found");
        }
        return getUserById(user.getId());
    }

    public User getUserById(int id) {
        Optional<User> user = userStorage.getItemById(id);
        return user.orElseThrow(
                () -> new NoSuchElementException("user with id = " + id + " not found")
        );
    }

    public List<User> getAllUsers() {
        return userStorage.getAllItems();
    }

    public void makeFriend(int firstFriendId, int secondFriendId) {
        getUserById(firstFriendId);
        getUserById(secondFriendId);
        userStorage.addFriend(firstFriendId, secondFriendId);
    }

    public void deleteFriend(int firstFriendId, int secondFriendId) {
        getUserById(firstFriendId);
        getUserById(secondFriendId);
        userStorage.deleteFriend(firstFriendId, secondFriendId);
    }

    public List<User> getUserFriends(int userId) {
        getUserById(userId);
        List<Integer> friendsId = userStorage.getUserFriends(userId);
        return friendsId.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }


    public List<User> getCommonFriend(int firstFriendId, int secondFriendId) {
        getUserById(firstFriendId);
        List<Integer> friendsIdFirst = userStorage.getUserFriends(firstFriendId);

        getUserById(secondFriendId);
        List<Integer> friendsIdSecond = userStorage.getUserFriends(secondFriendId);

        List<Integer> commonFriend = friendsIdFirst.stream()
                .filter(friendsIdSecond::contains)
                .collect(Collectors.toList());

        return commonFriend.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}

