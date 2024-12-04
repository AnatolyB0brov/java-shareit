package ru.practicum.shareit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validationgroup.Add;
import ru.practicum.shareit.validationgroup.Update;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private long id;

    @NotBlank(groups = {Add.class, Update.class})
    @Size(max = 1000, groups = {Add.class, Update.class})
    private String description;
}