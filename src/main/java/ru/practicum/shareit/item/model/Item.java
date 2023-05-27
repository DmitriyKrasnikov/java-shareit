package ru.practicum.shareit.item.model;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @NotNull(groups = {Create.class})
    @Min(groups = {Update.class}, value = 1L)
    Long id;
    @NotNull(groups = {Create.class})
    @Min(groups = {Update.class}, value = 1L)
    Long userId;
    @NotBlank(groups = {Create.class})
    String name;
    @NotBlank(groups = {Create.class})
    String description;
    @BooleanFlag
    @NotNull(groups = {Create.class})
    Boolean available;
}
