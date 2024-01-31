package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    @Autowired
    ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody Item item,
                              @RequestHeader(USER_HEADER) Long userId){
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/itemId")
    public ItemDto updateItem(@RequestBody Item item,
                              @RequestHeader(USER_HEADER) Long userId,
                              @PathVariable Long itemId){
        return itemService.updateItem(item, userId, itemId);
    }

    @GetMapping("/ItemId")
    public ItemDto getItemById(@PathVariable Long itemId){
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(Long userId){
        return itemService.getByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemBySubString(@RequestHeader(USER_HEADER) Long userId,
                                             @RequestParam(name = "text") String subString){
        return itemService.findByString(userId ,subString);
    }
}
