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
import ru.yandex.practicum.filmorate.web.dto.user.AddUserResponseDto;
import ru.yandex.practicum.filmorate.web.dto.user.GetUserResponseDto;
import ru.yandex.practicum.filmorate.web.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.web.dto.user.UpdateUserResponseDto;
import ru.yandex.practicum.filmorate.web.dto.user.UserDbEntity;
import ru.yandex.practicum.filmorate.web.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;
    private final ObjectMapper jacksonMapper = new ObjectMapper();


    @Autowired
    public UserController(
            UserService service,
            UserMapper userMapper) {
        this.service = service;
        this.mapper = userMapper;
    }

    @GetMapping("/{id}")
    public GetUserResponseDto getUserById(@PathVariable("id") int id) {
        User user = service.getUserById(id);
        return mapper.mapToGetUserResponseDto(user);
    }

    @GetMapping
    public List<User> getAllUsers() throws JsonProcessingException {
        log.info("Get request: GET {}", Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        List <User> responseBody = service.getAllUsers();
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(responseBody));
        return responseBody;
    }

    @PostMapping
    public AddUserResponseDto addUser(@RequestBody @Valid AddUserRequestDto userDto) {
        User user = mapper.mapToUser(userDto);
        user = service.add(user);
        return mapper.mapToAddUserResponseDto(user);
    }

    @PutMapping
    public UpdateUserResponseDto updateUser(@RequestBody @Valid UpdateUserRequestDto userDto) throws JsonProcessingException {
        log.info("Get request: PUT {}", Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        log.info("Request body: " + jacksonMapper.writeValueAsString(userDto));
        User user = mapper.mapToUser(userDto);
        user = service.update(user);
        UpdateUserResponseDto responseBody = mapper.mapToUpdateUserResponseDto(user);
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