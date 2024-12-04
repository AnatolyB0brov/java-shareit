package ru.practicum.shareit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDtoIn {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
