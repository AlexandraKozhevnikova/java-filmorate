package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:sql/schema.sql"})
class UserDeleteTest {

    private final UserService service;

    @Test
    void deleteUser() {
        User newUser = new User();
        newUser.setName("Test");
        newUser.setEmail("Test@gmail.com");
        newUser.setBirthday(LocalDate.now());
        newUser.setLogin("Login");

        User user = service.add(newUser);

        User userFromDB = service.getUserById(user.getId());

        service.deleteUser(user.getId());

        User deletedUser = null;
        try {
            deletedUser = service.getUserById(user.getId());
        } catch (Throwable e) {
        }

        Assertions.assertNull(deletedUser, "Пользователь не удален");
    }
}