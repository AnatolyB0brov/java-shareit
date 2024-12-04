package ru.practicum.shareit.mapper;

import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.dto.BookingItemDto;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.model.Booking;

public final class BookingMapper {

    private BookingMapper() {

    }

    public static BookingDto toBookingDtoShort(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId()
        );
    }

    public static Booking toBooking(BookingItemDto bookingItemDto, Booking booking) {
        booking.setStart(bookingItemDto.getStart());
        booking.setEnd(bookingItemDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }
}
