package ru.practicum.shareit.validator;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EndAndStartOfBookingValidator implements ConstraintValidator<EndAndStartOfBookingValidation, BookingDto> {

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        boolean isEndNull = (bookingDto.getEnd() == null);

        boolean isStartNull = (bookingDto.getStart() == null);

        if (isEndNull || isStartNull) {
            return false;
        }

        boolean isEndFutureOrPresent = bookingDto.getEnd().equals(LocalDateTime.now())
                || bookingDto.getEnd().isAfter(LocalDateTime.now());

        boolean isStartFutureOrPresent = bookingDto.getStart().equals(LocalDateTime.now())
                || bookingDto.getStart().isAfter(LocalDateTime.now());

        boolean isEndAfterStart = bookingDto.getEnd().isAfter(bookingDto.getStart());

        boolean isNotEndEqualStart = !bookingDto.getEnd().equals(bookingDto.getStart());

        return isEndFutureOrPresent
                && isStartFutureOrPresent
                && isEndAfterStart
                && isNotEndEqualStart;
    }
}