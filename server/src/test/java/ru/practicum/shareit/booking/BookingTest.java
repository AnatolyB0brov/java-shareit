package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingTest {

    @Test
    void testDefaultConstructor() {
        Booking booking = new Booking();
        assertNotNull(booking);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Item item = new Item();
        User user = new User();
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);

        assertEquals(1L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(user, booking.getBooker());
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        Booking booking = new Booking();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Item item = new Item();
        User user = new User();

        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        assertEquals(1L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(user, booking.getBooker());
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void testToString() {
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, null, BookingStatus.APPROVED);
        String expectedString = "Booking(id=1, start=" + booking.getStart() + ", end=" + booking.getEnd() + ", item=null, booker=null, status=APPROVED)";
        assertEquals(expectedString, booking.toString());
    }
}
