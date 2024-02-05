package ru.practicum.shareit.comment.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.comment.model.Comment;

public interface commentRepository extends CrudRepository<Comment, Long> {
}
