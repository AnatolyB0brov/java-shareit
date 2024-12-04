package ru.practicum.shareit.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingItemDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
