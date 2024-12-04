package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserDtoMapperTest {
    private final UserDto dto = new UserDto(1L, "User", "user@mail.ru");
    private final User us = new User(1L, "User", "user@mail.ru");

    @Test
    public void toUserDto() {
        UserDto userDto = UserMapper.toDto(us);
        assertThat(userDto, equalTo(dto));
    }

    @Test
    public void toUser() {
        User user = UserMapper.toEntity(dto);
        assertThat(user.getId(), equalTo(us.getId()));
        assertThat(user.getName(), equalTo(us.getName()));
        assertThat(user.getEmail(), equalTo(us.getEmail()));
    }
}
