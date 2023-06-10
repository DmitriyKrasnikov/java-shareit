package ru.practicum.shareit.booking.model;

import org.apache.catalina.mapper.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.entityInterfaces.EntityMapper;

public class BookingMapper implements EntityMapper<BookingDto,Booking> {
    @Override
    public BookingDto mapTo(Booking entity) {
        new BookingDto(entity.getId(),entity.getStartDate(),entity.getEndDate(),entity.getStatus(),
                entity.getBooker().getId(), entity.getItem().getId(), entity.getItem().getName());
    }

    @Override
    public Booking mapFrom(BookingDto entity) {
        return new ;
    }
}
