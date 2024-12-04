package ru.practicum.shareit.mapper;

import ru.practicum.shareit.dto.ItemRequestDtoIn;
import ru.practicum.shareit.dto.ItemRequestDtoOut;
import ru.practicum.shareit.model.ItemRequest;

public class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequest toItemRequest(ItemRequestDtoIn requestDtoIn) {
        return new ItemRequest(
                requestDtoIn.getDescription()
        );
    }

    public static ItemRequestDtoOut toItemRequestDtoOut(ItemRequest request) {
        return new ItemRequestDtoOut(
                request.getId(),
                request.getDescription(),
                request.getRequestor().getId(),
                request.getCreated()
        );
    }
}
