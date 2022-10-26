package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film add(Film item);

    Film update(Film newItem);

    List<Film> getAllItems();

    Film getItemById(int id);
}
