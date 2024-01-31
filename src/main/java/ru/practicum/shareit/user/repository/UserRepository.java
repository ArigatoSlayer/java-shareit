package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    UserDto createUser(User user);

    UserDto updateUser(Long id, User user);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    List<UserDto> getAllUsers();
}
