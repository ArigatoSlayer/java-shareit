package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {
    BookingDto.BookingDtoOut create(long bookerId, BookingDto bookingDto);

    BookingDto.BookingDtoOut getById(long userId, long id);

    Collection<BookingDto.BookingDtoOut> getAllBookerBookings(long bookerId, String state);

    Collection<BookingDto.BookingDtoOut> getAllItemBookingsForOwner(long ownerId, String state);

    BookingDto.BookingDtoOut update(long ownerId, long id, Boolean isApproved);
}