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
    public BookingDtoTo mapToWithClasses(Booking entity) {
        return new BookingDtoTo(entity.getId(), entity.getStartDate(), entity.getEndDate(), entity.getStatus(),
                new UserDtoForBooking(entity.getBooker().getId()), new ItemDtoForBooking(entity.getItem().getId(), entity.getItem().getName()));
    }

    @Override
    public BookingDtoFrom mapTo(Booking entity) {
       return new BookingDtoFrom(entity.getId(), entity.getStartDate(), entity.getEndDate(), entity.getStatus(),
                entity.getBooker().getId(), entity.getItem().getId());
    }

    @Override
    public Booking mapFrom(BookingDtoFrom entity) {
        User user = userRepository.findById(entity.getBookerId()).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id " + entity.getBookerId() + "не найден"));
        Item item = itemRepository.findById(entity.getItemId()).orElseThrow(() ->
                new EntityNotFoundException("Вещь с id " + entity.getItemId() + "не найдена"));
        Booking booking = new Booking(entity.getId(), entity.getStart(), entity.getEnd(), entity.getStatus(), user, item);
        return booking;
    }
}
