package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemDto> getByUserId(Long userId) {
        return itemRepository.getByUserId(userId);
    }

    public ItemDto createItem(Item item, Long userId) {
        return itemRepository.createItem(item, userId);
    }

    public ItemDto updateItem(Item item, Long userId, Long itemId) {
        return itemRepository.updateItem(item, userId, itemId);
    }

    public ItemDto getItemById(Long id) {
        return itemRepository.getItemById(id);
    }

    public List<ItemDto> findByString(Long userId, String subString) {
        return itemRepository.findByString(userId, subString);
    }
}
