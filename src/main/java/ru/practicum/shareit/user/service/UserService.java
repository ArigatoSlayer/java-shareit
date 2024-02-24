package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(User user) {
        return UserMapper.toUserDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, User user) {
        User thsisUser = getUserById(id);
        if (user.getName() != null) thsisUser.setName(user.getName());
        if (user.getEmail() != null) thsisUser.setEmail(user.getEmail());
        return UserMapper.toUserDto(userRepository.save(thsisUser));
    }

    public UserDto getUserDtoById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с Id = %s не найден", id))
        );
        return UserMapper.toUserDto(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с Id = %s не найден", id))
        );
    }
}
