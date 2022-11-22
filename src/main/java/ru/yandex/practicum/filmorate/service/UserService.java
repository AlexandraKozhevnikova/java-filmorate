package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.web.dto.user.UserDbEntity;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
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
        return Collections.emptyList(); //todo
        //return userStorage.getAllItems();
    }

    public void makeFriend(int firstFriendId, int secondFriendId) {
       User requester = getUserById(firstFriendId);
       User responser = getUserById(secondFriendId);






//        Set<Integer> friendsListFirst = firstFriend.getFriends();
//        friendsListFirst.add(secondFriend.getId());
//        firstFriend.setFriends(friendsListFirst);
//
//        Set<Integer> friendsListSecond = secondFriend.getFriends();
//        friendsListSecond.add(firstFriend.getId());
//        secondFriend.setFriends(friendsListSecond);
    }

    public void deleteFriend(User firstFriend, User secondFriend) {
        Set<Integer> friendsListFirst = firstFriend.getFriends();
        friendsListFirst.remove(secondFriend.getId());
        firstFriend.setFriends(friendsListFirst);

        Set<Integer> friendsListSecond = secondFriend.getFriends();
        friendsListSecond.remove(firstFriend.getId());
        secondFriend.setFriends(friendsListFirst);
    }

    public Set<Integer> getCommonFriend(User firstFriend, User secondFriend) {
        Set<Integer> firstUserFriends = firstFriend.getFriends();
        Set<Integer> secondUserFriends = secondFriend.getFriends();
        Set<Integer> result = firstUserFriends
                .stream()
                .filter(secondUserFriends::contains)
                .collect(Collectors.toSet());
        return result;
    }
}
