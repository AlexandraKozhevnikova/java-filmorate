package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Storage;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    Storage<User> storage = new Storage();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return storage.getAllItems();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody @Valid User user) {
        return storage.add(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody @Valid User user){
        return storage.update(user);
    }
}
