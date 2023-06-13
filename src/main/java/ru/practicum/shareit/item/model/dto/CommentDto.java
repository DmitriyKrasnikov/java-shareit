package ru.practicum.shareit.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validateInterfaces.Create;
import ru.practicum.shareit.validateInterfaces.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(groups = Create.class)
    @Size(groups = {Create.class, Update.class}, max = 1000)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
