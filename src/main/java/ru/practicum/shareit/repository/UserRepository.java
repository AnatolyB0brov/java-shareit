package ru.practicum.shareit.repository;

import ru.practicum.shareit.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    User save(User user);

    Optional<User> findById(long userId);

    void deleteById(long userId);
}
