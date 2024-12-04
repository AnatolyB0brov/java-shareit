package ru.practicum.shareit.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.client.BookingClient;
import ru.practicum.shareit.constant.Constants;
import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.dto.BookingState;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validationgroup.Add;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> saveNewBooking(@Validated(Add.class) @RequestBody BookingDto bookingDto,
                                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("POST / bookings");
        return bookingClient.saveBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable long bookingId,
                                          @RequestParam(name = "approved") Boolean isApproved,
                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("PATCH / bookings / {}", bookingId);
        return bookingClient.approve(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable long bookingId,
                                                 @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                 @RequestHeader(Constants.HEADER_USER_ID) long bookerId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("GET / ByBooker {}", bookerId);
        return bookingClient.getAllByBooker(from, size, state, bookerId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size,
                                                @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                @RequestHeader(Constants.HEADER_USER_ID) long ownerId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("GET / ByOwner / {}", ownerId);
        return bookingClient.getAllByOwner(from, size, state, ownerId);
    }
}