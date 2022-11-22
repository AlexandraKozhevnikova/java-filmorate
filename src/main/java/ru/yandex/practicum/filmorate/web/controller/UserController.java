package ru.yandex.practicum.filmorate.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.web.dto.user.AddUserRequestDto;
import ru.yandex.practicum.filmorate.web.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.web.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.web.mapper.UserMapper;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final ObjectMapper jacksonMapper = new ObjectMapper();

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable("id") int id) {
        User user = service.getUserById(id);
        return UserMapper.mapUserToResponseDto(user);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() throws JsonProcessingException {
        log.info("Get request: GET {}", Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        List<UserResponseDto> responseBody = service.getAllUsers().stream()
                .map(UserMapper::mapUserToResponseDto)
                .collect(Collectors.toList());
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(responseBody));
        return responseBody;
    }

    @PostMapping
    public UserResponseDto addUser(@RequestBody @Valid AddUserRequestDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        user = service.add(user);
        return UserMapper.mapUserToResponseDto(user);
    }

    @PutMapping
    public UserResponseDto updateUser(@RequestBody @Valid UpdateUserRequestDto userDto) throws JsonProcessingException {
        log.info("Get request: PUT {}", Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        log.info("Request body: " + jacksonMapper.writeValueAsString(userDto));
        User user = UserMapper.mapToUser(userDto);
        user = service.update(user);
        UserResponseDto responseBody = UserMapper.mapUserToResponseDto(user);
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(responseBody));
        return responseBody;
    }

//    @PutMapping("/{id}/friends/{friendId}")
//    public User makeFriend(
//            @PathVariable("id") int id,
//            @PathVariable("friendId") int friendId
//    ) {
//        if (id == friendId) {
//            throw new BadFriendshipException("'id' should not be equal 'friendId'");
//        }
//        service.makeFriend(id, friendId);
//        return user;
//    }

//    @DeleteMapping("/{id}/friends/{friendId}")
//    public User deleteFriend(
//            @PathVariable("id") int id,
//            @PathVariable("friendId") int friendId
//    ) {
//        User user = storage.getItemById(id);
//        User friend = storage.getItemById(friendId);
//        service.deleteFriend(user, friend);
//        return user;
//    }
//
//    @GetMapping("/{id}/friends")
//    public Set<User> getUserFriends(@PathVariable("id") int id) {
//        User user = storage.getItemById(id);
//        return user.getFriends().stream()
//                .map(storage::getItemById)
//                .collect(Collectors.toSet());
//    }
//
//    @GetMapping("/{id}/friends/common/{otherId}")
//    public Set<User> getCommonFriends(
//            @PathVariable("id") int id,
//            @PathVariable("otherId") int otherId
//    ) {
//        User user = storage.getItemById(id);
//        User friend = storage.getItemById(otherId);
//        return service.getCommonFriend(user, friend).stream()
//                .map(storage::getItemById)
//                .collect(Collectors.toSet());
//    }
}