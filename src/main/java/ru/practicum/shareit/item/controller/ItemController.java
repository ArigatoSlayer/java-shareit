package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USER_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_HEADER) long ownerId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(ownerId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader(USER_HEADER) long userId,
                        @PathVariable long id) {
        return itemService.getItemById(userId, id);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsByUser(@RequestHeader(USER_HEADER) long ownerId) {
        return itemService.getAllItemsByUser(ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader(USER_HEADER) long ownerId,
                          @RequestBody ItemDto itemDto, @PathVariable long id) {
        return itemService.updateItem(ownerId, itemDto, id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_HEADER) long authorId,
                                    @Valid @RequestBody CommentDto commentDto, @PathVariable long itemId) {
        return itemService.createComment(authorId, commentDto, itemId);
    }

}