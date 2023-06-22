package ru.practicum.shareit.request.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.dto.ItemDtoForRequest;
import ru.practicum.shareit.validateInterfaces.Create;
import ru.practicum.shareit.validateInterfaces.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    @Size(groups = {Create.class, Update.class}, min = 1, max = 255)
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime created;
    private List<ItemDtoForRequest> items;
}
