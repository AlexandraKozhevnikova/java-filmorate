package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Storage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "управление пользователями")
public class UserController {

    private Storage<User> storage = new Storage();

    @Operation(
            summary = "Получение списка всех пользователей"
    )
    @GetMapping
    public List<User> getAllUsers() {
        return storage.getAllItems();
    }

    @Operation(
            summary = "Добавление пользователя",
            description = "нет проверки на уникальность"
    )
    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        return storage.add(user);
    }

    @Operation(
            summary = "Обновление пользователя",
            description = "для существующих пользователей"
    )
    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return storage.update(user);
    }
}
