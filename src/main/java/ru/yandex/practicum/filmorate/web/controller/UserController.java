package ru.yandex.practicum.filmorate.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadFriendshipException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.web.dto.film.*;
import ru.yandex.practicum.filmorate.web.dto.user.AddUserRequest;
import ru.yandex.practicum.filmorate.web.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.web.dto.user.UserResponse;
import ru.yandex.practicum.filmorate.web.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.web.mapper.UserMapper;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final FeedService feedService;
    private final ObjectMapper jacksonMapper = new ObjectMapper();

    @Autowired
    public UserController(UserService service, FeedService feedService) {
        this.service = service;
        this.feedService = feedService;
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") int id) {
        User user = service.getUserById(id);
        return UserMapper.mapUserToResponse(user);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() throws JsonProcessingException {
        log.info("Get request: GET {}", Arrays.stream(this.getClass().getAnnotation(RequestMapping.class)
                .value())
                .findFirst()
                .get());
        List<UserResponse> responseBody = service.getAllUsers().stream()
                .map(UserMapper::mapUserToResponse)
                .collect(Collectors.toList());
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(responseBody));
        return responseBody;
    }

    @PostMapping
    public UserResponse addUser(@RequestBody @Valid AddUserRequest userDto) {
        User user = UserMapper.mapToUser(userDto);
        user = service.add(user);
        return UserMapper.mapUserToResponse(user);
    }

    @PutMapping
    public UserResponse updateUser(@RequestBody @Valid UpdateUserRequest userDto) throws JsonProcessingException {
        log.info("Get request: PUT {}", Arrays.stream(this.getClass().getAnnotation(RequestMapping.class).value()).findFirst().get());
        log.info("Request body: " + jacksonMapper.writeValueAsString(userDto));
        User user = UserMapper.mapToUser(userDto);
        user = service.update(user);
        UserResponse responseBody = UserMapper.mapUserToResponse(user);
        log.info("Response status code: 200 ОК");
        log.info("Response body: {}", jacksonMapper.writeValueAsString(responseBody));
        return responseBody;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String makeFriend(
            @PathVariable("id") int id,
            @PathVariable("friendId") int friendId
    ) {
        if (id == friendId) {
            throw new BadFriendshipException("'id' should not be equal 'friendId'");
        }
        service.makeFriend(id, friendId);
        return "success";
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String  deleteFriend(
            @PathVariable("id") int id,
            @PathVariable("friendId") int friendId
    ) {
        service.deleteFriend(id, friendId);
        return "success";
    }

    @GetMapping("/{id}/friends")
    public List<UserResponse> getUserFriends(@PathVariable("id") int id) {
        List<User> friends = service.getUserFriends(id);

        return friends.stream()
                .map(UserMapper::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserResponse> getCommonFriends(
            @PathVariable("id") int firstFriendId,
            @PathVariable("otherId") int secondFriendId
    ) {
           List<User> common = service.getCommonFriend(firstFriendId, secondFriendId);
        return common.stream()
                .map(UserMapper::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") int userId) {
        service.deleteUser(userId);
    }


    @GetMapping("/{id}/recommendations")
    public List<FilmResponse> getRecommendations(@PathVariable("id") int id) {
        List<Film> recommendations = service.getFilmRecommendations(id);
        return recommendations.stream()
                .map(FilmMapper::mapFilmToFilmResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/feed")
    public List<Feed> getFeedById(@PathVariable("id") int id) {
        return feedService.getFeedById(id);
    }

}