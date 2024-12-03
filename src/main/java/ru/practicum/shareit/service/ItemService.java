package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDtoIn;
import ru.practicum.shareit.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {
    ItemDtoOut getItemById(long itemId, long userId);

    List<ItemDtoOut> getItemsByOwner(long userId);

    List<ItemDtoOut> getItemBySearch(String text);

    ItemDtoOut addItem(ItemDtoIn itemDtoIn, long userId);

    ItemDtoOut updateItem(long itemId, ItemDtoIn itemDtoIn, long userId);

    CommentDto addComment(long itemId, CommentDto commentDto, long userId);
}
