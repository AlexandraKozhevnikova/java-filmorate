package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    public void makeFriend(User firstFriend, User secondFriend) {
        firstFriend.friends.add(secondFriend.getId());
        secondFriend.friends.add(firstFriend.getId());
    }

    public void deleteFriend(User firstFriend, User secondFriend) {
        firstFriend.friends.remove(secondFriend.getId());
        secondFriend.friends.remove(firstFriend.getId());
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
