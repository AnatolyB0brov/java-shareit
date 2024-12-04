package ru.practicum.shareit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.validationgroup.Add;
import ru.practicum.shareit.validationgroup.Update;

@Data
public class CommentDto {
    @Size(max = 1000, groups = {Add.class, Update.class})
    @NotBlank(groups = {Add.class})
    private String text;
}
