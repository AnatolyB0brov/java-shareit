package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.Constants;
import ru.practicum.shareit.dto.BookingItemDto;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.service.BookingService;
import ru.practicum.shareit.validationgroup.Add;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking addBooking(@Validated(Add.class) @RequestBody BookingItemDto bookingItemDtoIn,
                              @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.addBooking(bookingItemDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approve(@PathVariable long bookingId, @RequestParam(name = "approved") Boolean isApproved,
                           @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.approve(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable long bookingId,
                                  @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllByBooker(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                        @RequestHeader(Constants.HEADER_USER_ID) long bookerId) {
        return bookingService.getAllByBooker(state, bookerId);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                       @RequestHeader(Constants.HEADER_USER_ID) long ownerId) {
        return bookingService.getAllByOwner(ownerId, state);
    }
}