package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.user.AddUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.exception.BadFriendshipException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Storage<User> storage;
    private final UserService service;

    @Autowired
    public UserController(Storage<User> storage, UserService service) {
        this.storage = storage;
        this.service = service;
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

    @PutMapping("/{id}/friends/{friendId}")
    public User makeFriend(
            @PathVariable("id") int id,
            @PathVariable("friendId") int friendId
    ) {
        if (id == friendId) {
            throw new BadFriendshipException("'id' should not be equal 'friendId'");
        }
        User user = storage.getItemById(id);
        User friend = storage.getItemById(friendId);
        service.makeFriend(user, friend);
        return user;
    }
}