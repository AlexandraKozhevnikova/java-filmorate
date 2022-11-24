package ru.yandex.practicum.filmorate.componentTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.DbUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTest {
    private final DbUserStorage userStorage;

    @Test
    public void findUserByIdTest() {
        User user = User.builder()
                .name("Harry")
                .login("hp")
                .email("hp@ya.ru")
                .birthday(LocalDate.now())
                .build();

        user.setId(userStorage.add(user));

        Optional<User> userOptional = userStorage.getItemById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}