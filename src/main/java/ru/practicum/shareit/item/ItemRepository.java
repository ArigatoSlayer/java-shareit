package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findItemsByOwnerId(Long ownerId);

    @Query(value = "select i from Item as i " +
            "where i.available = true " +
            "and lower(i.name) like lower(concat('%', ?1, '%') ) or " +
            "lower(i.description) like lower(concat( '%', ?1, '%'))")
    List<Item> search(String text);
}
