package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.BookingItemDto;
import ru.practicum.shareit.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(BookingItemDto bookingItemDto, long userId);

    Booking approve(long bookingId, Boolean isApproved, long userId);

    Booking getBookingById(long bookingId, long userId);

    List<Booking> getAllByBooker(String subState, long bookerId);

    List<Booking> getAllByOwner(long ownerId, String state);
}
