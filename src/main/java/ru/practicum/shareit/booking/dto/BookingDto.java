package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.validator.EndAndStartOfBookingValidation;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EndAndStartOfBookingValidation
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long itemId;

    @Data
    @AllArgsConstructor
    public static class BookingDtoOut {
        private long id;
        private LocalDateTime start;
        private LocalDateTime end;
        private ItemDto item;
        private UserDto booker;
        private BookingStatus status;
    }

    @Data
    @AllArgsConstructor
    public static class BookingDtoForOwner {
        private long id;
        private long bookerId;
    }
}