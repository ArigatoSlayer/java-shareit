package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(User user) {
        return userRepository.createUser(user);
    }

    public UserDto updateUser(Long id, User user) {
        return userRepository.updateUser(id, user);
    }

    public UserDto getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
