package ru.practicum.shareit.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoFrom {
    private long id;
    @NotNull(groups = {Create.class})
    @FutureOrPresent(groups = {Create.class})
    LocalDateTime start;
    @NotNull(groups = {Create.class})
    @Future(groups = {Create.class})
    LocalDateTime end;
    BookingStatus status;
    long bookerId;
    @NotNull(groups = {Create.class})
    long itemId;
}
