package ru.practicum.shareit.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.dto.ItemDtoForBooking;
import ru.practicum.shareit.user.model.dto.UserDtoForBooking;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoTo {
    private long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
    UserDtoForBooking booker;
    ItemDtoForBooking item;
}
