package ru.practicum.shareit.exceptions;

public class ItemAvailabilityException extends RuntimeException {
    public ItemAvailabilityException(String message) {
        super(message);
    }
}