package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDto;
import ru.yandex.practicum.filmorate.dto.user.AddUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.AddUserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.GetUserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserResponseDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {

    public User mapToUser(AddUserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        return user;
    }

    public User mapToUser(UpdateUserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        user.setId(dto.getId());
        return user;
    }

    public AddUserResponseDto mapToAddUserResponseDto(User user) {
        AddUserResponseDto userDto = new AddUserResponseDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }

    public UpdateUserResponseDto mapToUpdateUserResponseDto(User user) {
        UpdateUserResponseDto userDto = new UpdateUserResponseDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }

    public GetUserResponseDto mapToGetUserResponseDto(User user) {
        GetUserResponseDto userDto = new GetUserResponseDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }
}
