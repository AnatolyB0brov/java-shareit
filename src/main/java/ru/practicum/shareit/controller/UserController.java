package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.validationgroup.Add;
import ru.practicum.shareit.validationgroup.Update;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto createUser(@Validated(Add.class) @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }


    @PatchMapping("/{userId}")
    public UserDto updateUserById(@PathVariable long userId, @Validated(Update.class) @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

}
