package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.user.AddUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.AddUserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.GetUserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserResponseDto;
import ru.yandex.practicum.filmorate.exception.BadFriendshipException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Storage<User> storage;
    private final UserService service;
    private final UserMapper userMapper;

    @Autowired
    public UserController(
            Storage<User> storage,
            UserService service,
            UserMapper userMapper) {
        this.storage = storage;
        this.service = service;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public GetUserResponseDto getUserById(@PathVariable("id") int id) {
        User user = storage.getItemById(id);
        return userMapper.mapToGetUserResponseDto(user);
    }


    @GetMapping
    public List<User> getAllUsers() {
        return storage.getAllItems();
    }

    @PostMapping
    public AddUserResponseDto addUser(@RequestBody @Valid AddUserRequestDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        user = storage.add(user);
        return userMapper.mapToAddUserResponseDto(user);
    }

    @PutMapping
    public UpdateUserResponseDto updateUser(@RequestBody @Valid UpdateUserRequestDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        user = storage.update(user);
        return userMapper.mapToUpdateUserResponseDto(user);
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

    //DELETE /users/{id}/friends/{friendId}
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(
            @PathVariable("id") int id,
            @PathVariable("friendId") int friendId
    ) {
        User user = storage.getItemById(id);
        User friend = storage.getItemById(friendId);
        service.deleteFriend(user, friend);
        return user;
    }

    @GetMapping("/{id}/friends")
    public Set<User> getUserFriends(@PathVariable("id") int id) {
        User user = storage.getItemById(id);
        return user.getFriends().stream()
                .map(storage::getItemById)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(
            @PathVariable("id") int id,
            @PathVariable("otherId") int otherId
    ) {
        User user = storage.getItemById(id);
        User friend = storage.getItemById(otherId);
        return service.getCommonFriend(user, friend).stream()
                .map(storage::getItemById)
                .collect(Collectors.toSet());
    }
}