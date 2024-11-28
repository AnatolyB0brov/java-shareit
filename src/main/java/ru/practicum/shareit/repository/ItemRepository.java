package ru.practicum.shareit.repository;

import ru.practicum.shareit.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> findById(long itemId);

    List<Item> findItemsByOwner(long userId);

    List<Item> findItemByText(String text);

    Item save(Item item, long userId);
}
