package ru.practicum.shareit.mapper;

import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.model.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toEntity(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
