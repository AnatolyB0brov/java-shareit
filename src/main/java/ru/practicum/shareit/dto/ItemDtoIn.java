package ru.practicum.shareit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validationgroup.Add;

@Data
@Builder
public class ItemDtoIn {
    private long id;
    @NotBlank(groups = {Add.class})
    private String name;
    @NotBlank(groups = {Add.class})
    private String description;
    @NotNull(groups = {Add.class})
    private Boolean available;
}
