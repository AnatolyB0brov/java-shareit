package ru.practicum.shareit.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.client.RequestClient;
import ru.practicum.shareit.constant.Constants;
import ru.practicum.shareit.dto.RequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validationgroup.Add;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> saveNewRequest(@Validated(Add.class) @RequestBody RequestDto requestDto,
                                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("POST / requests {} / user {}", requestDto.getDescription(), userId);
        return requestClient.saveRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByRequestor(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("GET / requests / requestor {}", userId);
        return requestClient.getRequestsByRequestor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size,
                                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("GET / requests");
        return requestClient.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId,
                                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("GET / request {} / user {}", requestId, userId);
        return requestClient.getRequestById(requestId, userId);
    }
}