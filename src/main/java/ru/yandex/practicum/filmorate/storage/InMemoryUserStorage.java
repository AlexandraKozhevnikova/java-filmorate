package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage{
}
