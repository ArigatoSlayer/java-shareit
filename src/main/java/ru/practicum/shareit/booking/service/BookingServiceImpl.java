package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemAvailabilityException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto.BookingDtoOut create(long bookerId, BookingDto bookingDto) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + bookerId + " не найден"));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDto.getItemId() + " не найдена"));

        if (item.getOwner().getId() == bookerId) {
            throw new NotFoundException("Владелец вещи не может забронировать свою же вещь");
        }

        if (!item.getAvailable()) {
            throw new ItemAvailabilityException("Вещь с id " + bookingDto.getItemId()
                    + " недоступна к бронированию");
        }

        Booking booking = BookingMapper.mapToBooking(bookingDto, item, booker, BookingStatus.WAITING);

        return BookingMapper.mapToBookingDtoOutput(bookingRepository.save(booking));
    }

    @Override
    public BookingDto.BookingDtoOut getById(long userId, long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + id + " не найдено"));

        long bookerId = booking.getBooker().getId();
        long ownerId = booking.getItem().getOwner().getId();

        if (bookerId != userId && ownerId != userId) {
            throw new NotFoundException("Бронирование с id " + id + " не найдено для пользователя с id " + userId);
        }

        return BookingMapper.mapToBookingDtoOutput(booking);
    }

    @Override
    public Collection<BookingDto.BookingDtoOut> getAllBookerBookings(long bookerId, String state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + bookerId + " не найден"));

        BookingState bookingState = BookingState.valueOf(state);

        switch (bookingState) {
            case CURRENT:
                return bookingRepository.readAllBookerCurrentBookings(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.readAllBookerPastBookings(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.readAllBookerFutureBookings(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository
                        .findAllByBooker_IdAndStatusInOrderByStartDesc(bookerId, List.of(BookingStatus.WAITING))
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository
                        .findAllByBooker_IdAndStatusInOrderByStartDesc(bookerId, List.of(BookingStatus.REJECTED))
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findAllByBooker_IdOrderByStartDesc(bookerId)
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<BookingDto.BookingDtoOut> getAllItemBookingsForOwner(long ownerId, String state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + ownerId + " не найден"));

        List<Long> userItems = itemRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        BookingState bookingState = BookingState.valueOf(state);
        switch (bookingState) {
            case CURRENT:
                return bookingRepository.readAllOwnerItemsCurrentBookings(userItems, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.readAllOwnerItemsPastBookings(userItems, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.readAllOwnerItemsFutureBookings(userItems, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository
                        .findAllByItem_IdInAndStatusInOrderByStartDesc(userItems, List.of(BookingStatus.WAITING))
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository
                        .findAllByItem_IdInAndStatusInOrderByStartDesc(userItems, List.of(BookingStatus.REJECTED))
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findAllByItem_IdInOrderByStartDesc(userItems)
                        .stream()
                        .map(BookingMapper::mapToBookingDtoOutput)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public BookingDto.BookingDtoOut update(long ownerId, long id, Boolean isApproved) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + id + " не найдено"));

        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new NotFoundException("Пользователь с id " + ownerId
                    + " не может редактировать бронирование с id " + id);
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ItemAvailabilityException("Бронирование уже подтверждено");
        }

        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.mapToBookingDtoOutput(bookingRepository.save(booking));
    }
}