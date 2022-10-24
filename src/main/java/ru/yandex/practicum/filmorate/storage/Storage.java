package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface Storage<T> {

    T add(T item);

    T update(T newItem);

    List<T> getAllItems();

    T getItemById(int id);
}
