package ru.practicum.shareit.booking.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoFrom;
import ru.practicum.shareit.booking.model.dto.BookingDtoTo;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDtoForBooking;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
public class BookingMapperImpl implements BookingMapper {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingMapperImpl(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingDtoTo mapToWithClasses(Booking b) {
        return new BookingDtoTo(b.getId(), b.getStartDate(), b.getEndDate(), b.getStatus(),
                new UserDtoForBooking(b.getBooker().getId()),
                new ItemDtoForBooking(b.getItem().getId(), b.getItem().getName()));
    }

    @Override
    public BookingDtoFrom mapTo(Booking b) {
        return new BookingDtoFrom(b.getId(), b.getStartDate(), b.getEndDate(), b.getStatus(), b.getBooker().getId(),
                b.getItem().getId());
    }

    @Override
    public Booking mapFrom(BookingDtoFrom entity) {
        User user = userRepository.findById(entity.getBookerId()).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id " + entity.getBookerId() + "не найден"));

        Item item = itemRepository.findById(entity.getItemId()).orElseThrow(() ->
                new EntityNotFoundException("Вещь с id " + entity.getItemId() + "не найдена"));

        return new Booking(entity.getId(), entity.getStart(), entity.getEnd(), entity.getStatus(), user, item);
    }
}
