package ru.practicum.shareit.mapper;

import ru.practicum.shareit.dto.ItemDtoIn;
import ru.practicum.shareit.dto.ItemDtoOut;
import ru.practicum.shareit.model.Item;

public final class ItemMapper {
    public static ItemDtoOut toDto(Item item) {
        return new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner()
        );
    }

    public static Item toItem(ItemDtoIn itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
                );
    }
}
