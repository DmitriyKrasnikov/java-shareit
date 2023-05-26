package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.entityInterfaces.EntityMapper;

@Component
public class BookingMapper implements EntityMapper<BookingDto, Booking> {

    @Override
    public BookingDto mapTo(Booking entity) {
        return null;
    }

    @Override
    public Booking mapFrom(BookingDto entity) {
        return null;
    }
}
