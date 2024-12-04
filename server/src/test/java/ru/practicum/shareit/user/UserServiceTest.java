package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private final long id = 1L;
    private final UserDto userDto = new UserDto(id, "User", "user@mail.ru");
    private final User user = new User(id, "User", "user@mail.ru");


    @Test
    void findAll_whenNoUsers_thenReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<UserDto> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }


    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> targetUsers = userService.getAllUsers();

        Assertions.assertNotNull(targetUsers);
        assertEquals(1, targetUsers.size());
        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void getUserById_whenUserFound_thenReturnedUser() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto actualUser = userService.getUserById(id);

        assertEquals(UserMapper.toDto(user), actualUser);
    }

    @Test
    void getUserById_whenUserNotFound_thenExceptionThrown() {
        when((userRepository).findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void saveNewUser_whenUserNameValid_thenSavedUser() {
        when(userRepository.save(any())).thenReturn(user);

        UserDto actualUser = userService.addUser(userDto);

        assertEquals(userDto, actualUser);
    }

    @Test
    void saveNewUser_whenEmailNotUnique_thenThrowsDuplicateEmailException() {
        when(userRepository.save(any())).thenThrow(new DuplicateEmailException("Email already exists"));

        assertThrows(DuplicateEmailException.class, () -> {
            userService.addUser(userDto);
        });
    }

    @Test
    void saveNewUser_whenUserEmailDuplicate_thenNotSavedUser() {
        doThrow(DataIntegrityViolationException.class).when(userRepository).save(any(User.class));

        assertThrows(DataIntegrityViolationException.class, () -> userService.addUser(userDto));
    }

    @Test
    void updateUser_whenUserFound_thenUpdatedOnlyAvailableFields() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto actualUser = userService.updateUser(id, userDto);

        assertEquals(UserMapper.toDto(user), actualUser);
        verify(userRepository, times(1))
                .findById(user.getId());
    }

    @Test
    void deleteUser() {
        userService.deleteUserById(1L);
        verify(userRepository, times(1))
                .deleteById(1L);
    }

    @Test
    public void testValidateUniqueEmail_EmailExists() {
        when(userRepository.findAll()).thenReturn(List.of(new User(id, "Some User", "user@mail.ru")));

        DuplicateEmailException thrown = assertThrows(DuplicateEmailException.class, () -> {
            userService.addUser(userDto);
        });

        assertEquals("Пользователь с email user@mail.ru уже существует", thrown.getMessage());
    }

    @Test
    public void testValidateUniqueEmail_EmailDoesNotExist() {
        UserDto uniqueUserDto = new UserDto(id, "Unique User", "unique@example.com");
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        assertDoesNotThrow(() -> userService.addUser(uniqueUserDto));
    }

    @Test
    void updateUser_whenUserNotFound_thenThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(id, userDto));
    }


    @Test
    void updateUser_whenUserNameIsBlank_thenUserNameNotUpdated() {
        UserDto userDtoWithBlankName = new UserDto(id, "   ", "newemail@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto actualUser = userService.updateUser(id, userDtoWithBlankName);

        assertEquals(user.getName(), actualUser.getName()); // Имя не должно измениться
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void updateUser_whenUserEmailIsBlank_thenUserEmailNotUpdated() {
        UserDto userDtoWithBlankEmail = new UserDto(id, "New Name", "   ");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto actualUser = userService.updateUser(id, userDtoWithBlankEmail);

        assertEquals(user.getEmail(), actualUser.getEmail()); // Email не должен измениться
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void updateUser_whenUserEmailIsNotUnique_thenThrowsDuplicateEmailException() {
        UserDto userDtoWithDuplicateEmail = new UserDto(id, "New Name", "duplicate@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of(new User(id, "Existing User", "duplicate@example.com")));

        assertThrows(DuplicateEmailException.class, () -> userService.updateUser(id, userDtoWithDuplicateEmail));
    }

    @Test
    void deleteUserById_whenUserDoesNotExist_thenThrowsEntityNotFoundException() {
        doThrow(new EntityNotFoundException("Пользователь не найден")).when(userRepository).deleteById(anyLong());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(2L));
    }

}
