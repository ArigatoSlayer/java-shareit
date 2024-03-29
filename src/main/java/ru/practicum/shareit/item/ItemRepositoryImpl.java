package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
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
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private Long id = 0L;
    private final Map<Long, Item> itemMap = new HashMap<>();

    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Item item, Long userId) {
        userRepository.getUserById(userId);
        item.setOwner(userId);
        item.setId(++id);
        itemMap.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Item item, Long userId, Long itemId) {
        checkItem(itemId);
        userRepository.getUserById(userId);
        Item oldItem = itemMap.get(itemId);
        if (!oldItem.getOwner().equals(userId)) {
            throw new NotFoundException("Вы не являетесь владельцем данного предмета");
        }
        if (item.getName() != null) oldItem.setName(item.getName());
        if (item.getDescription() != null) oldItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) oldItem.setAvailable(item.getAvailable());
        return ItemMapper.toItemDto(oldItem);
    }

    @Override
    public List<ItemDto> getByUserId(Long userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long id) {
        checkItem(id);
        return ItemMapper.toItemDto(itemMap.get(id));
    }

    @Override
    public List<ItemDto> findByString(Long userId, String subString) {
        String normalString = subString.toLowerCase().strip();
        List<ItemDto> findItems = new ArrayList<>();
        if (!subString.isBlank()) {
            findItems.addAll(itemMap.values()
                    .stream()
                    .filter(item -> item.getAvailable().equals(true))
                    .filter(item -> item.getName().toLowerCase().contains(normalString) ||
                            item.getDescription().toLowerCase().contains(normalString))
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList()));
        }
        return findItems;
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        userRepository.getUserById(userId);
        if (itemMap.get(itemId).getOwner().equals(userId)) {
            itemMap.remove(itemId);
        } else {
            throw new NotValidException("Вы не являетесь владельцем данного предмета");
        }
    }

    private void checkItem(Long id) {
        try {
            itemMap.get(id);
        } catch (NullPointerException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
