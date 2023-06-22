package ru.practicum.shareit.item.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    @NotBlank(groups = Create.class, message = "не должно быть пустым")
    @Size(groups = {Create.class, Update.class}, max = 1000)
    private String text;
    private String authorName;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime created;
}
