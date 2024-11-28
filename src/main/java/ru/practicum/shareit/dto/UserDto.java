package ru.practicum.shareit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validationgroup.Add;

@Data
@Builder
public class UserDto {
    private long id;
    @NotBlank(groups = {Add.class})
    private String name;
    @NotEmpty(groups = {Add.class})
    @Email(groups = {Add.class})
    private String email;
}
