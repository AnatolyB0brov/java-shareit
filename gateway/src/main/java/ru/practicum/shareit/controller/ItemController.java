package ru.practicum.shareit.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.constant.Constants;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.validationgroup.Add;

import java.util.ArrayList;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> saveNewItem(@Validated(Add.class) @RequestBody ItemDto itemDto,
                                              @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("POST / items {} / user {}", itemDto.getName(), userId);
        return itemClient.saveNewItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                                             @Validated(Add.class) @RequestBody ItemDto itemDto,
                                             @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("PATCH / items {} / user {}", itemId, userId);
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("GET / items {} / user {}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                  @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("GET / items / user {}", userId);
        return itemClient.getItemsByOwner(from, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemBySearch(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                  @RequestParam String text,
                                                  @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("GET / search / {}", text);
        if (text.isBlank()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return itemClient.getItemBySearch(from, size, text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveNewComment(@PathVariable long itemId,
                                                 @Validated(Add.class) @RequestBody CommentDto commentDto,
                                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("POST / comment / item {}", itemId);
        return itemClient.saveNewComment(itemId, commentDto, userId);
    }
}