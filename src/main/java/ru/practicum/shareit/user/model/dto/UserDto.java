package ru.practicum.shareit.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validateInterfaces.Create;
import ru.practicum.shareit.validateInterfaces.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    @Size(groups = {Create.class, Update.class}, max = 512)
    private String email;
    @Size(groups = {Create.class, Update.class}, max = 255)
    @NotBlank(groups = {Create.class})
    private String name;
}
