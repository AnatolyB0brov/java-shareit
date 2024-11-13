package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.ItemDto;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.validationgroup.Add;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwnerList(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestParam String text) {
        return itemService.getItemByText(text);
    }

    @PostMapping
    public ItemDto addItem(@Validated(Add.class) @RequestBody ItemDto itemDto,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemId, itemDto, userId);
    }
}
