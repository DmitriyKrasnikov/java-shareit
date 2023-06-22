package ru.practicum.shareit.user.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.validateInterfaces.Create;
import ru.practicum.shareit.validateInterfaces.Update;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {UserDto.class})
public class UserDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeserializeValidDto() throws IOException {
        String json = "{\"id\":1,\"email\":\"user@example.com\",\"name\":\"John Doe\"}";

        UserDto userDto = objectMapper.readValue(json, UserDto.class);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getEmail()).isEqualTo("user@example.com");
        assertThat(userDto.getName()).isEqualTo("John Doe");
    }

    @Test
    public void testDeserializeInvalidEmail() throws IOException {
        String json = "{\"id\":1,\"email\":\"invalid-email\",\"name\":\"John Doe\"}";

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(objectMapper.readValue(json, UserDto.class),
                Create.class, Update.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("должно иметь формат адреса" +
                " электронной почты");
    }

    @Test
    public void testDeserializeEmptyName() throws IOException {
        String json = "{\"id\":1,\"email\":\"user@example.com\",\"name\":\"\"}";

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(objectMapper.readValue(json, UserDto.class),
                Create.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("не должно быть пустым");
    }
}