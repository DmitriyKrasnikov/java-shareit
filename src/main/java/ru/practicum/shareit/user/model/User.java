package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @NotNull(groups = {Create.class})
    @Min(groups = {Update.class}, value = 1L)
    Long id;
    @EqualsAndHashCode.Include
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class})
    String email;
    @NotBlank(groups = {Create.class})
    String name;
    HashSet<Long> items;
}
