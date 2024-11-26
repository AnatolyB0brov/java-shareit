package ru.practicum.shareit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.WrongOwnerException;
import ru.practicum.shareit.dto.ItemDto;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto findById(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект с id = %s не найден", itemId)));
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(long userId) {
        userService.getUserById(userId);
        return itemRepository.findItemsByOwner(userId).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findItemByText(text).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        return ItemMapper.toDto(itemRepository.save(ItemMapper.toItem(itemDto), userId));
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект с id = %s не найден", itemId)));
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (item.getOwnerId() != userId) {
            throw new WrongOwnerException(String.format("Пользователь с id %s не является владельцем %s",
                    userId, name));
        }
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        if (available != null) {
            item.setAvailable(available);
        }
        return ItemMapper.toDto(item);
    }
}
