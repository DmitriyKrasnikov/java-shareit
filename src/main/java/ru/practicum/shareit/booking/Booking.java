package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Booking {
    LocalDate startDate;
    LocalDate endDate;
    Boolean approval;
}
