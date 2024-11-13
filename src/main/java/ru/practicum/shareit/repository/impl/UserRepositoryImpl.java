package ru.practicum.shareit.repository.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private static int userId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void deleteById(long userId) {
        if (!users.containsKey(userId)) {
           throw new EntityNotFoundException(String.format("Объект класса %s не найден", User.class));
        }
        users.remove(userId);
    }
}
