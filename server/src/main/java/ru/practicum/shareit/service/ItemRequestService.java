package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.ItemRequestDtoIn;
import ru.practicum.shareit.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut saveNewRequest(ItemRequestDtoIn requestDtoIn, long userId);

    List<ItemRequestDtoOut> getRequestsByRequestor(long userId);

    List<ItemRequestDtoOut> getAllRequests(Integer from, Integer size, long userId);

    ItemRequestDtoOut getRequestById(long requestId, long userId);

}
