package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    ItemDto createItem(Item item, Long userId);

    ItemDto updateItem(Item item, Long userId, Long itemId);

    List<ItemDto> getByUserId(Long userId);

    ItemDto getItemById(Long id);

    List<ItemDto> findByString(Long userId, String subString);

    void deleteItem(Long itemId, Long userId);
}
