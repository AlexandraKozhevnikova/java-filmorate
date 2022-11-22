package ru.yandex.practicum.filmorate.web.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.web.dto.user.AddUserRequestDto;
import ru.yandex.practicum.filmorate.web.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.web.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {

    public static User mapToUser(AddUserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        return user;
    }

    public static User mapToUser(UpdateUserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        user.setId(dto.getId());
        return user;
    }

    public static UserResponseDto mapUserToResponseDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }
}
