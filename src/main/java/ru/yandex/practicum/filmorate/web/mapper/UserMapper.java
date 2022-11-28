package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.web.dto.user.AddUserRequest;
import ru.yandex.practicum.filmorate.web.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.web.dto.user.UserResponse;

public class UserMapper {
    public static User mapToUser(AddUserRequest dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        return user;
    }

    public static User mapToUser(UpdateUserRequest dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        user.setId(dto.getId());
        return user;
    }

    public static UserResponse mapUserToResponse(User user) {
        UserResponse userDto = new UserResponse();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }
}
