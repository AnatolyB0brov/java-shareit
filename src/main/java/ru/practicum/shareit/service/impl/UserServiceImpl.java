package ru.practicum.shareit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Пользователь с id %s не найден", id)));
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        validateUniqueEmail(userDto);
        User user = userRepository.save(UserMapper.toEntity(userDto));
        return UserMapper.toDto(user);
    }


    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Пользователь с id %s не найден", id)));
        String name = userDto.getName();
        String email = userDto.getEmail();

        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        if (email != null && !email.isBlank()) {
            if (!user.getEmail().equals(userDto.getEmail())) {
                validateUniqueEmail(userDto);
            }
            user.setEmail(email);
        }
        return UserMapper.toDto(user);
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    private void validateUniqueEmail(UserDto userDto) {
        if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new DuplicateEmailException(String.format("Пользователь с email %s уже существует",
                    userDto.getEmail()));
        }
    }
}
