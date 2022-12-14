package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RecommendationsTest {

    private final UserService userService;

    @Test
    public void shouldReturnEmptyListWhenFullOverlapsTest() {
        assertTrue(userService.getFilmRecommendations(1).isEmpty(),
                "список рекомендаций должен быть пустым");
    }

    @Test
    public void shouldReturnTwoRecommendationTest() {
        List<Film> recommendation = userService.getFilmRecommendations(2);
        List<Integer> recommendationId = new ArrayList<>();
        recommendation.stream()
                .map(Film::getId)
                .forEach(recommendationId::add);

        assertEquals(2, recommendation.size(),
                "список рекомендаций должен содержать два рекомендованных фильма");

        assertIterableEquals(List.of(3, 10), recommendationId);
    }

    @Test
    public void shouldReturnEmptyListWhenUserWithoutLikesTest() {
        assertTrue(userService.getFilmRecommendations(5).isEmpty(),
                "список рекомендаций должен быть пустым");
    }

    @Test
    public void shouldReturnEmptyListWhenUserWithOneSingleLikesTest() {
        assertTrue(userService.getFilmRecommendations(6).isEmpty(),
                "список рекомендаций должен быть пустым");
    }

    @Test
    public void shouldReturnFourRecommendationTest() {
        List<Film> recommendation = userService.getFilmRecommendations(4);
        List<Integer> recommendationId = new ArrayList<>();
        recommendation.stream()
                .map(Film::getId)
                .forEach(recommendationId::add);

        assertEquals(4, recommendation.size(),
                "список рекомендаций должен содержать четыре рекомендованных фильма");

        assertIterableEquals(List.of(1, 2, 3, 10), recommendationId);
    }
}