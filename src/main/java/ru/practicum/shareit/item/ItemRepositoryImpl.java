package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Long id = 0L;
    private final Map<Long, Item> itemMap = new HashMap<>();

    @Autowired
    UserRepository userRepository;

    @Override
    public ItemDto createItem(Item item, Long userId) {
        item.setId(++id);
        itemMap.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Item item, Long userId, Long itemId) {
        userRepository.getUserById(userId);
        if (item.getOwner() != userId) {
            throw new NotValidException("Вы не являетесь владельцем данного предмета");
        }
        Item oldItem = itemMap.get(itemId);
        if (item.getName() != null) oldItem.setName(item.getName());
        if (item.getDescription() != null) oldItem.setDescription(item.getDescription());
        if (item.getOwner() != null) oldItem.setOwner(item.getOwner());
        if (item.getAvailable() != null) oldItem.setAvailable(item.getAvailable());
        return ItemMapper.toItemDto(oldItem);
    }

    @Override
    public List<ItemDto> getByUserId(Long userId) {
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(itemMap.values());
        return allItems.stream().filter(item -> item.getOwner() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long id) {
        return null;
    }

    @Override
    public List<ItemDto> findByString(Long userId, String subString) {
        return null;
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {

    }
}
