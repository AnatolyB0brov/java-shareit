package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(long id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    void deleteUserById(long id);
}
