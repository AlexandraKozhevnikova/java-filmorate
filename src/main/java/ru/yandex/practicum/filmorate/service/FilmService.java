package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Map<Integer, Set<Integer>> likesStorage = new HashMap<>();


    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void like(int filmId, int userId) {
        filmStorage.getItemById(filmId);
        userStorage.getItemById(userId);
        if (likesStorage.containsKey(filmId)) {
            likesStorage.get(filmId).add(userId);
        } else {
            likesStorage.put(filmId, new HashSet<>(List.of(userId)));
        }
    }

    public void unlike(int filmId, int userId) {
        filmStorage.getItemById(filmId);
        userStorage.getItemById(userId);
        if (likesStorage.containsKey(filmId)) {
            likesStorage.get(filmId).remove(userId);
        }
    }

    public Set<Film> getTopFilms(int threshold) {
        Set<Film> filmList = likesStorage.entrySet().stream()
                .sorted(Comparator.comparingInt(it -> (-1) * it.getValue().size()))
                .sorted()
                .filter(it -> it.getValue().size() > 0)
                .limit(threshold)
                .map(Map.Entry::getKey)
                .map(filmStorage::getItemById)
                .collect(Collectors.toSet());

        if (filmList.size() < threshold) {
            filmList.addAll(filmStorage.getAllItems());
            filmList = filmList.stream()
                    .limit(threshold)
                    .collect(Collectors.toSet());
        }
        return filmList;
    }
}
