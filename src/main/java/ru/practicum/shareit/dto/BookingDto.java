package ru.practicum.shareit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private long bookerId;
}