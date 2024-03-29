package ru.practicum.shareit.request.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.model.dto.ItemDtoForRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void serializeJson() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("Test description");
        dto.setCreated(LocalDateTime.parse("2023-06-22T15:23:51.4094029"));

        List<ItemDtoForRequest> items = new ArrayList<>();

        ItemDtoForRequest itemDto = new ItemDtoForRequest();
        itemDto.setId(1L);
        itemDto.setName("Test item");
        items.add(itemDto);
        dto.setItems(items);

        String expectedJson = "{\"id\":1,\"description\":\"Test description\",\"created\":" +
                "\"2023-06-22T15:23:51.4094029\",\"items\":[{\"id\":1,\"name\":\"Test item\",\"description\":null," +
                "\"available\":null,\"requestId\":null}]}";

        String actualJson = objectMapper.writeValueAsString(dto);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void deserializeJson() throws Exception {
        String content = "{\"id\":1,\"description\":\"Test description\",\"created\":\"2023-06-22T12:20:16.832\"," +
                "\"items\":[{\"id\":1,\"name\":\"Test item\"}]}";

        ItemRequestDto dto = objectMapper.readValue(content, ItemRequestDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Test description");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.parse("2023-06-22T12:20:16.832"));
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getItems().get(0).getId()).isEqualTo(1L);
        assertThat(dto.getItems().get(0).getName()).isEqualTo("Test item");
    }
}