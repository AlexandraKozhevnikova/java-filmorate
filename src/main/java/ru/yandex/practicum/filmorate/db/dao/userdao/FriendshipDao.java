package ru.yandex.practicum.filmorate.db.dao.userdao;

import java.util.List;

public interface FriendshipDao {

    void addFriend(int firstFriend, int secondFriend);

    void deleteFriend(int firstFriend, int secondFriend);

    List<Integer> getUserFriends(int userId);
}
