package ru.practicum.shareit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDtoOut {
    private long id;
    private String description;
    private long requestorId;
    private LocalDateTime created;
    private List<ItemDtoOut> items;

    public ItemRequestDtoOut(long id, String description, long requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }
}