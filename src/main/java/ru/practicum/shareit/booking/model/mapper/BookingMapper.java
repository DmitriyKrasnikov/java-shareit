package ru.practicum.shareit.booking.model.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoFrom;
import ru.practicum.shareit.booking.model.dto.BookingDtoTo;
import ru.practicum.shareit.entityInterfaces.EntityMapper;

public interface BookingMapper extends EntityMapper<BookingDtoFrom, Booking> {
    BookingDtoTo mapToWithClasses(Booking entity);
}
