package ru.practicum.shareit.booking.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.validateInterfaces.Create;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class BookingDtoFromTest {
    @Autowired
    private ObjectMapper objectMapper;

    private final Long ID = 1L;
    private final LocalDateTime START = LocalDateTime.of(2023, 6, 22, 14, 30);
    private final LocalDateTime END = LocalDateTime.of(2023, 6, 22, 15, 0);
    private final BookingStatus STATUS = BookingStatus.WAITING;
    private final Long BOOKER_ID = 2L;
    private final Long ITEM_ID = 3L;

    @Test
    public void testSerialize() throws Exception {
        BookingDtoFrom bookingDtoFrom = new BookingDtoFrom();
        bookingDtoFrom.setId(ID);
        bookingDtoFrom.setStart(START);
        bookingDtoFrom.setEnd(END);
        bookingDtoFrom.setStatus(STATUS);
        bookingDtoFrom.setBookerId(BOOKER_ID);
        bookingDtoFrom.setItemId(ITEM_ID);

        String expectedJson = "{\"id\":1,\"start\":\"2023-06-22T14:30:00\",\"end\":\"2023-06-22T15:00:00\"," +
                "\"status\":\"WAITING\",\"bookerId\":2,\"itemId\":3}";
        String actualJson = objectMapper.writeValueAsString(bookingDtoFrom);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"start\":\"2023-06-22T14:30:00\",\"end\":\"2023-06-22T15:00:00\",\"status" +
                "\":\"WAITING\",\"bookerId\":2,\"itemId\":3}";

        BookingDtoFrom expectedBookingDtoFrom = new BookingDtoFrom();
        expectedBookingDtoFrom.setId(ID);
        expectedBookingDtoFrom.setStart(START);
        expectedBookingDtoFrom.setEnd(END);
        expectedBookingDtoFrom.setStatus(STATUS);
        expectedBookingDtoFrom.setBookerId(BOOKER_ID);
        expectedBookingDtoFrom.setItemId(ITEM_ID);

        BookingDtoFrom actualBookingDtoFrom = objectMapper.readValue(json, BookingDtoFrom.class);
        assertThat(actualBookingDtoFrom).isEqualTo(expectedBookingDtoFrom);
    }

    @Test
    public void testBookingDtoFromValidationSuccess() {
        BookingDtoFrom dto = new BookingDtoFrom(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING,
                1L, 2L);
        Set<ConstraintViolation<BookingDtoFrom>> violations = Validation.buildDefaultValidatorFactory().getValidator()
                .validate(dto, Create.class);
        assertEquals(0, violations.size());
    }

    @Test
    public void testBookingDtoFromValidationStartNull() {
        BookingDtoFrom dto = new BookingDtoFrom(1L,
                null,
                LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING,
                1L, 2L);
        Set<ConstraintViolation<BookingDtoFrom>> violations = Validation.buildDefaultValidatorFactory().getValidator()
                .validate(dto, Create.class);
        assertEquals(1, violations.size());
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("не должно равняться null");
    }

    @Test
    public void testBookingDtoFromValidationStartPast() {
        BookingDtoFrom dto = new BookingDtoFrom(1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING,
                1L, 2L);
        Set<ConstraintViolation<BookingDtoFrom>> violations = Validation.buildDefaultValidatorFactory().getValidator()
                .validate(dto, Create.class);
        assertEquals(1, violations.size());
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("должно содержать сегодняшнее число" +
                " или дату, которая еще не наступила");
    }

    @Test
    public void testBookingDtoFromValidationEndNull() {
        BookingDtoFrom dto = new BookingDtoFrom(1L,
                LocalDateTime.now().plusDays(1),
                null, BookingStatus.WAITING,
                1L, 2L);
        Set<ConstraintViolation<BookingDtoFrom>> violations = Validation.buildDefaultValidatorFactory().getValidator()
                .validate(dto, Create.class);
        assertEquals(1, violations.size());
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("не должно равняться null");
    }

    @Test
    public void testBookingDtoFromValidationEndPast() {
        BookingDtoFrom dto = new BookingDtoFrom(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1),
                BookingStatus.WAITING,
                1L, 2L);
        Set<ConstraintViolation<BookingDtoFrom>> violations = Validation.buildDefaultValidatorFactory().getValidator()
                .validate(dto, Create.class);
        assertEquals(1, violations.size());
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("должно содержать дату, которая еще" +
                " не наступила");
    }
}