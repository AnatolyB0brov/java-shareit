package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto findById(long itemId);

    List<ItemDto> getItemsByOwner(long userId);

    List<ItemDto> getItemByText(String text);

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto updateItem(long itemId, ItemDto itemDto, long userId);
}
