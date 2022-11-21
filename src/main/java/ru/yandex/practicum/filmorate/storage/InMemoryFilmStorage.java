//package ru.yandex.practicum.filmorate.storage;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//@Component
//@Qualifier("inMemoryFilmStorage")
//public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
//
//    private final Map<Integer, Set<Integer>> likesStorage = new HashMap<>();
//
////    public Set<Film> getTopFilms(int threshold) {
////        Set<Film> filmList = likesStorage.entrySet().stream()
////                .sorted(Comparator.comparingInt(it -> (-1) * it.getValue().size()))
////                .limit(threshold)
////                .map(Map.Entry::getKey)
////                .map(filmStorage::getItemById)
////                .map(Optional::get)// todo   подумать  так как поменялся на оптионал
////                .collect(Collectors.toSet());
////
////        if (filmList.size() < threshold) {
////            filmList.addAll(filmStorage.getAllItems());
////            filmList = filmList.stream()
////                    .limit(threshold)
////                    .collect(Collectors.toSet());
////        }
////        return filmList;
////    }
//}
