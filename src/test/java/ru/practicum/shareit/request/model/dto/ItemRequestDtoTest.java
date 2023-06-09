package ru.practicum.shareit.request.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.item.model.dto.ItemDtoForRequest;
import ru.practicum.shareit.validateInterfaces.Create;
import ru.practicum.shareit.validateInterfaces.Update;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Validator validator;

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


    @Test
    public void shouldValidateSuccessfully() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Valid description");
        itemRequestDto.setCreated(LocalDateTime.now());

        List<ItemDtoForRequest> items = new ArrayList<>();

        items.add(new ItemDtoForRequest());
        itemRequestDto.setItems(items);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void shouldFailValidationWhenDescriptionIsNull() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription(null);
        itemRequestDto.setCreated(LocalDateTime.now());

        List<ItemDtoForRequest> items = new ArrayList<>();

        items.add(new ItemDtoForRequest());
        itemRequestDto.setItems(items);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto,
                Create.class, Update.class);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("не должно быть пустым");
    }

    @Test
    public void shouldFailValidationWhenDescriptionIsEmpty() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("");
        itemRequestDto.setCreated(LocalDateTime.now());

        List<ItemDtoForRequest> items = new ArrayList<>();

        items.add(new ItemDtoForRequest());
        itemRequestDto.setItems(items);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto,
                Create.class, Update.class);

        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    public void shouldFailValidationWhenDescriptionIsTooLong() {
        String longDescription = "*".repeat(256);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription(longDescription);
        itemRequestDto.setCreated(LocalDateTime.now());

        List<ItemDtoForRequest> items = new ArrayList<>();

        items.add(new ItemDtoForRequest());
        itemRequestDto.setItems(items);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto,
                Create.class, Update.class);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("размер должен находиться в диапазоне от 1 до 255");
    }

    @Configuration
    static class CommentDtoTestConfig {
        @Bean
        public Validator validator() {
            return Validation.buildDefaultValidatorFactory().getValidator();
        }
    }
}