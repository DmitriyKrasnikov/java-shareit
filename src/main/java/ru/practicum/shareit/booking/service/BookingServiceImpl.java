package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.dto.BookingDtoFrom;
import ru.practicum.shareit.booking.model.dto.BookingDtoTo;
import ru.practicum.shareit.booking.model.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public BookingDtoTo createBooking(BookingDtoFrom bookingDtoFrom, long userId) {
        log.info("Create booking");

        if (!bookingDtoFrom.getStart().isBefore(bookingDtoFrom.getEnd())) {
            throw new BadRequestException("Неверные временные показатели брони");
        }

        bookingDtoFrom.setBookerId(userId);
        bookingDtoFrom.setStatus(BookingStatus.WAITING);

        Booking bookingBefore = bookingMapper.mapFrom(bookingDtoFrom);

        if (bookingBefore.getItem().getUser().getId().equals(bookingBefore.getBooker().getId())) {
            throw new EntityNotFoundException("Собственник вещи не может ее забронировать");
        }

        if (!bookingBefore.getItem().getAvailable()) {
            throw new BadRequestException(String.format("Вещь с id %d в данный момент недоступна для брони",
                    bookingBefore.getItem().getId()));
        }
        return bookingMapper.mapToWithClasses(bookingRepository.save(bookingBefore));
    }

    @Transactional
    @Override
    public BookingDtoTo makeApprovedBooking(long userId, long bookingId, boolean approved) {
        log.info("Make approved booking {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Бронь с id %d не найдена", bookingId)));

        if (booking.getItem().getUser().getId() != userId) {
            throw new EntityNotFoundException(String.format("Вы не можете подтвердить бронь, так как не являетесь " +
                    "владельцем вещи: %s", booking.getItem().getName()));
        }

        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        if (booking.getStatus() == newStatus) {
            throw new BadRequestException(String.format("Статус уже %s", newStatus.name().toLowerCase()));
        }
        booking.setStatus(newStatus);

        return bookingMapper.mapToWithClasses(booking);
    }

    @Override
    public BookingDtoTo getBooking(long userId, long bookingId) {
        log.info("get Booking {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Бронь с id %d не найдена", bookingId)));

        if (booking.getBooker().getId() != userId && booking.getItem().getUser().getId() != userId) {
            throw new EntityNotFoundException("У вас нет доступа к этой брони");
        }
        return bookingMapper.mapToWithClasses(booking);
    }

    @Override
    public List<BookingDtoTo> getUserBookings(long userId, String state) {
        log.info("Get users booking {}", userId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findBookingsByBookerId(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsByBookerId(userId, LocalDateTime.now());
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsByBookerId(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsByBookerId(userId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.findWaitingBookingsByBookerId(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findRejectedBookingsByBookerId(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (bookings.isEmpty()) {
            throw new EntityNotFoundException(String.format("По данному пользователю %d ничего не найдено", userId));
        }

        return bookings.stream().map(bookingMapper::mapToWithClasses).collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoTo> getOwnerBookings(long userId, String state) {
        log.info("Get owner bookings {}", userId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDateDesc(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsByItemOwnerIdOrderByStartDateDesc(userId,
                        LocalDateTime.now());
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsByItemOwnerIdOrderByStartDateDesc(userId,
                        LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsByItemOwnerIdOrderByStartDateDesc(userId,
                        LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId,
                        BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId,
                        BookingStatus.REJECTED);
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (bookings.isEmpty()) {
            throw new EntityNotFoundException(String.format("По данному пользователю %d ничего не найдено", userId));
        }
        return bookings.stream().map(bookingMapper::mapToWithClasses).collect(Collectors.toList());
    }
}
