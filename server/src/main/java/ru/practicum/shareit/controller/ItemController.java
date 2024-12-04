package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.Constants;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDtoIn;
import ru.practicum.shareit.dto.ItemDtoOut;
import ru.practicum.shareit.service.ItemService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDtoOut addItem(@RequestBody ItemDtoIn itemDtoIn,
                              @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.addItem(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@PathVariable long itemId, @RequestBody ItemDtoIn itemDtoIn,
                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.updateItem(itemId, itemDtoIn, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@PathVariable long itemId, @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getItemsByOwner(@RequestParam(defaultValue = "1") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.getItemsByOwner(from, size, userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> getFilmBySearch(@RequestParam(defaultValue = "1") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam String text) {
        return itemService.getItemBySearch(from, size, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId, @RequestBody CommentDto commentDto,
                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.addComment(itemId, commentDto, userId);
    }
}
