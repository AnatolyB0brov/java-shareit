package ru.practicum.shareit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validationgroup.Add;
import ru.practicum.shareit.validationgroup.Update;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;
    @Size(max = 1000, groups = {Add.class, Update.class})
    @NotBlank(groups = {Add.class})
    private String text;
    private String authorName;
    private LocalDateTime created;
}