package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public List<ItemDto> getAllItemByUserId(Long userId) {
        if (itemRepository.findAll().iterator().next() == null){
            return new ArrayList<>();
        } else {
            return itemRepository.findItemsByOwnerId(userId)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    public ItemDto createItem(Item item, Long userId) {
        User user = userService.getUserById(userId);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(Item item, Long userId, Long itemId) {
        Item originalItem = getItemById(itemId);
        if (originalItem.getOwner().getId().equals(userId)) {
            if (item.getAvailable() != null) originalItem.setAvailable(item.getAvailable());
            if (item.getName() != null) originalItem.setName(item.getName());
            if (item.getDescription() != null) originalItem.setDescription(item.getDescription());
            itemRepository.save(originalItem);
            return ItemMapper.toItemDto(originalItem);
        } else {
            throw new NotValidException(("Вы не являетесь владельцем данной вещи"));
        }
    }

    public ItemDto getItemDtoById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
                "предмет с id = %s не найден", id
        )));
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> findBySubstring(Long userId, String text) {
        if (!text.isEmpty()) {
            return itemRepository.search(text)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public void deleteItemById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    private Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
                "предмет с id = %s не найден", id
        )));
    }

}
