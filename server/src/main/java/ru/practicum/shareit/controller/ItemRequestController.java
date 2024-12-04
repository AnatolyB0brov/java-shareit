package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.Constants;
import ru.practicum.shareit.dto.ItemRequestDtoIn;
import ru.practicum.shareit.dto.ItemRequestDtoOut;
import ru.practicum.shareit.service.ItemRequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDtoOut saveNewRequest(@RequestBody ItemRequestDtoIn requestDtoIn,
                                            @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return requestService.saveNewRequest(requestDtoIn, userId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getRequestsByRequestor(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return requestService.getRequestsByRequestor(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequests(@RequestParam(defaultValue = "1") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return requestService.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getRequestById(@PathVariable long requestId,
                                            @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return requestService.getRequestById(requestId, userId);
    }
}
