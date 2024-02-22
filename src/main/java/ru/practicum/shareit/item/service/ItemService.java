package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(long ownerId, ItemDto itemDto);

    ItemDto getItemById(long userId, long id);

    Collection<ItemDto> getAllItemsByUser(long ownerId);

    ItemDto updateItem(long ownerId, ItemDto itemDto, long id);

    Collection<ItemDto> search(String text);

    CommentDto createComment(long authorId, CommentDto commentDto, long itemId);
}