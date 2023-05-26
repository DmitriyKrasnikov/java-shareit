package ru.practicum.shareit.item.model;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Min(1)
    Long id;
    @Min(1)
    Long userId;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @BooleanFlag
    @NotNull
    Boolean available;
}
