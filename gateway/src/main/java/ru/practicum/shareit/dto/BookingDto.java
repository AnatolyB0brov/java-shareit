package ru.practicum.shareit.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validationgroup.Add;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    @FutureOrPresent(groups = {Add.class})
    @NotNull(groups = {Add.class})
    private LocalDateTime start;

    @Future(groups = {Add.class})
    @NotNull(groups = {Add.class})
    private LocalDateTime end;

    @NotNull(groups = {Add.class})
    private Long itemId;
}
