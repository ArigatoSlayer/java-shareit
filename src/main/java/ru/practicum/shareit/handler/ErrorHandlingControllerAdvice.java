package ru.practicum.shareit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.ItemAvailabilityException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DisruptionErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Disruption> disruptions = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Disruption(error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new DisruptionErrorResponse(disruptions);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Disruption handleNotFoundException(NotFoundException e) {
        return new Disruption(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Disruption handleAccessDeniedException(AccessDeniedException e) {
        return new Disruption(e.getMessage());
    }

    @ExceptionHandler(ItemAvailabilityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Disruption handleItemAvailabilityException(ItemAvailabilityException e) {
        return new Disruption(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Disruption handleAnyException(Throwable e) {
        return new Disruption(e.getMessage());
    }
}