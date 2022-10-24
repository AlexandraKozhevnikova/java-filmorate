package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.ComparableComparator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private Storage<Film> filmStorage;
    private Storage<User> userStorage;
    private final Map<Integer, Set<Integer>> likesStorage = new HashMap<>();


    @Autowired
    public FilmService(Storage<Film> filmStorage, Storage<User> userStorage) {
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

    public List<Film> getTopFilms(int threshold) {
        List<Film> filmList = likesStorage.entrySet().stream()
                .sorted(Comparator.comparingInt(it -> it.getValue().size()))
                .filter(it -> it.getValue().size() > 0)
                .limit(threshold)
                .map(Map.Entry::getKey)
                .map(filmStorage::getItemById)
                .collect(Collectors.toList());
        return filmList;
    }
}
