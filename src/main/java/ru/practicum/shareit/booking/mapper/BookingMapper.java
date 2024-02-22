package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public Booking mapToBooking(BookingDto bookingDto, Item item, User booker, BookingStatus status) {
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker,
                status);
    }

    public BookingDto.BookingDtoOut mapToBookingDtoOutput(Booking booking) {
        return new BookingDto.BookingDtoOut(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.mapToItemDto(booking.getItem(), null, null, null),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus());
    }

    public BookingDto.BookingDtoForOwner maptoBookingDtoForOwner(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingDto.BookingDtoForOwner(booking.getId(),
                booking.getBooker().getId());
    }

}