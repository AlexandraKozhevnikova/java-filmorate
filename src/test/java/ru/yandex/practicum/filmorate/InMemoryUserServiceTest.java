package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.NoSuchElementException;

public class InMemoryUserServiceTest {

    private final UserStorage userStorage = new InMemoryUserStorage();
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserService userService = new UserService(filmStorage, userStorage);

    @Test
    public void addUserTest() {
        User user = User.builder()
                .login("brain")
                .email("brain@ya.ru")
                .birthday(LocalDate.now())
                .build();

        user = userService.add(user);
        Assertions.assertEquals(1, user.getId());
    }

    @Test
    public void updateUserTest() {
        dataPreparation();

        User user = User.builder()
                .id(1)
                .login("PIN")
                .email("pinki@ya.ru")
                .birthday(LocalDate.now())
                .build();

        userService.update(user);
        Assertions.assertEquals(
                "PIN",
                userStorage.getItemById(1).getName());
    }

    @Test
    public void updateNotExistUserTest() {
        User user = User.builder()
                .id(123)
                .login("pinki")
                .email("pinki@ya.ru")
                .birthday(LocalDate.now())
                .build();

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.update(user)
        );
    }

    @Test
    public void getNotExistUserTest() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.getUserById(123)
        );
    }

    @Test
    public void getExistUserTest() {
        dataPreparation();
        User user = userService.getUserById(1);
        Assertions.assertEquals("pinki@ya.ru", user.getEmail());
    }

    @Test
    public void getAllFilm() {
        dataPreparation();
        dataPreparation();
        dataPreparation();

        Assertions.assertEquals(3, userStorage.getAllItems().size());
    }

    private void dataPreparation() {
        User user = User.builder()
                .login("pinki")
                .email("pinki@ya.ru")
                .birthday(LocalDate.now())
                .build();

        userService.add(user);
    }
}
