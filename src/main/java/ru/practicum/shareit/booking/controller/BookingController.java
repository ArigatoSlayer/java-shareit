package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.UnsupportedStatusException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto.BookingDtoOut create(@RequestHeader(USER_HEADER) long bookerId,
                                   @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(bookerId, bookingDto);
    }

    @GetMapping("/{id}")
    public BookingDto.BookingDtoOut getById(@RequestHeader(USER_HEADER) long userId,
                                 @PathVariable long id) {
        return bookingService.getById(userId, id);
    }

    @GetMapping
    public Collection<BookingDto.BookingDtoOut> getAllBookerBookings(@RequestHeader(USER_HEADER) long userId,
                                                              @RequestParam(defaultValue = "ALL") String state) {
        BookingState.from(state)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));

        return bookingService.getAllBookerBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto.BookingDtoOut> getAllItemBookingsForOwner(@RequestHeader(USER_HEADER) long ownerId,
                                                                 @RequestParam(defaultValue = "ALL") String state) {
        BookingState.from(state)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));

        return bookingService.getAllItemBookingsForOwner(ownerId, state);
    }

    @PatchMapping("/{id}")
    public BookingDto.BookingDtoOut update(@RequestHeader(USER_HEADER) long ownerId,
                                           @PathVariable long id,
                                           @RequestParam("approved") Boolean isApproved) {
        return bookingService.update(ownerId, id, isApproved);
    }
}