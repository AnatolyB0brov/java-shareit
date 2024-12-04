package ru.practicum.shareit.exception;

public class BookingNotOwnerException extends RuntimeException {
    public BookingNotOwnerException(String message) {
        super(message);
    }
}
