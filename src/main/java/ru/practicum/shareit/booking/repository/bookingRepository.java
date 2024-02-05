package ru.practicum.shareit.booking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

@Repository
public interface bookingRepository extends CrudRepository<Booking, Long> {

}
