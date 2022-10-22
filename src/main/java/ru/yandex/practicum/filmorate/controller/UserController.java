package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.user.AddUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Storage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Storage<User> storage;

    @Autowired
    public UserController(Storage<User> storage) {
        this.storage = storage;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return storage.getAllItems();
    }

    @PostMapping
    public User addUser(@RequestBody @Valid AddUserRequestDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        return storage.add(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid UpdateUserRequestDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        return storage.update(user);
    }
}