package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    public void makeFriend(User firstFriend, User secondFriend) {
        Set<Integer> friendsListFirst = firstFriend.getFriends();
        friendsListFirst.add(secondFriend.getId());
        firstFriend.setFriends(friendsListFirst);

        Set<Integer> friendsListSecond = secondFriend.getFriends();
        friendsListSecond.add(firstFriend.getId());
        secondFriend.setFriends(friendsListSecond);
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
