package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.dto.BookingDtoFrom;
import ru.practicum.shareit.booking.model.dto.BookingDtoTo;
import ru.practicum.shareit.booking.model.mapper.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    public BookingDtoTo createBooking(BookingDtoFrom bookingDtoFrom, long userId) {
        if(!bookingDtoFrom.getStart().isBefore(bookingDtoFrom.getEnd())){
            throw new BadRequestException("Неверные временные показатели брони");
        }
        bookingDtoFrom.setBookerId(userId);
        bookingDtoFrom.setStatus(BookingStatus.WAITING);
        Booking bookingBefore = bookingMapper.mapFrom(bookingDtoFrom);
        if(bookingBefore.getItem().getUser().getId().equals(bookingBefore.getBooker().getId())){
            throw new EntityNotFoundException("Собственник вещи не может ее забронировать");
        }
        if(!bookingBefore.getItem().getAvailable()){
            throw new BadRequestException("Вещь с id" + bookingBefore.getItem().getId() + "данный момент недоступна для брони");
        }
        Booking bookingAfter = bookingRepository.save(bookingBefore);
        BookingDtoTo bookingDtoFrom1 = bookingMapper.mapToWithClasses(bookingAfter);
        return bookingDtoFrom1;
    }

    public BookingDtoTo makeApprovedBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException("Бронь с id " + bookingId + " не найдена"));

        if (booking.getItem().getUser().getId() != userId) {
            throw new EntityNotFoundException("Вы не можете подтвердить бронь, так как не являетесь владельцем вещи: "
                    + booking.getItem().getName());
        }
        if (approved) {
            if(booking.getStatus().equals(BookingStatus.APPROVED)){
                throw new BadRequestException("Status already approved");
            }
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            if(booking.getStatus().equals(BookingStatus.REJECTED)){
                throw new BadRequestException("Status already rejected");
            }
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.mapToWithClasses(bookingRepository.save(booking));
    }

    public BookingDtoTo getBooking(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Бронь" +
                " с id " + bookingId + " не найдена"));
        if (booking.getBooker().getId() != userId && booking.getItem().getUser().getId() != userId) {
            throw new EntityNotFoundException("У вас нет доступа к этой брони");
        }
        return bookingMapper.mapToWithClasses(booking);
    }

    public List<BookingDtoTo> getUserBookings(long userId, String state) {
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

        if(bookings.isEmpty()){
            throw new EntityNotFoundException("По данному пользователю " + userId + " ничего не найдено");
        }

        return bookings.stream().map(bookingMapper::mapToWithClasses).collect(Collectors.toList());
    }

    public List<BookingDtoTo> getOwnerBookings(long userId, String state) {
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDateDesc(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsByItemOwnerIdOrderByStartDateDesc(userId, LocalDateTime.now());
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsByItemOwnerIdOrderByStartDateDesc(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsByItemOwnerIdOrderByStartDateDesc(userId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }

        if(bookings.isEmpty()){
            throw new EntityNotFoundException("По данному пользователю " + userId + " ничего не найдено");
        }
        return bookings.stream().map(bookingMapper::mapToWithClasses).collect(Collectors.toList());
    }
}
