package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.Constants;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDtoIn;
import ru.practicum.shareit.dto.ItemDtoOut;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.validationgroup.Add;
import ru.practicum.shareit.validationgroup.Update;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDtoOut addItem(@Validated(Add.class) @RequestBody ItemDtoIn itemDtoIn,
                              @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.addItem(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@PathVariable long itemId,
                                 @Validated(Update.class) @RequestBody ItemDtoIn itemDtoIn,
                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.updateItem(itemId, itemDtoIn, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@PathVariable long itemId, @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getItemsByOwner(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> getFilmBySearch(@RequestParam String text) {
        return itemService.getItemBySearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @Validated(Add.class) @RequestBody CommentDto commentDto,
                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemService.addComment(itemId, commentDto, userId);
    }
}
