package ru.yandex.practicum.filmorate.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage{
    @Override
    public void addFriend(int firstFriend, int secondFriend) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public void deleteFriend(int firstFriend, int secondFriend) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public List<Integer> getUserFriends(int userId) {
        throw new NotImplementedException("метод реализован только для БД");
    }

    @Override
    public void deleteUser(int userId) {
        throw new NotImplementedException("метод реализован только для БД");
    }
}
