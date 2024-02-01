package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public UserDto createUser(User user) {
        checkEmail(user);
        user.setId(++id);
        userMap.put(user.getId(), user);
        log.info("Создан пользователь с id = {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, User user) {
        getUserById(id);
        if (user.getEmail() != null) {
            checkEmail(user);
            userMap.get(id).setEmail(user.getEmail());
        } else {
            userMap.get(id).setEmail(userMap.get(id).getEmail());
        }
        if (user.getName() != null) userMap.get(id).setName(user.getName());
        log.info("Обновлен пользователь с id = {}", user.getId());
        return UserMapper.toUserDto(userMap.get(id));
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Получен пользователь с id = {}", id);
        return UserMapper.toUserDto(userMap.get(id));
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Удален пользователь с id = {}", id);
        userMap.remove(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получены все пользователи");
        return userMap.values()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void checkEmail(User user) {
        boolean isEmailNotUnique = userMap.values().stream()
                .anyMatch(thisUser -> thisUser.getEmail().equals(user.getEmail())
                        && !thisUser.getId().equals(user.getId()));
        if (isEmailNotUnique) {
            throw new NotValidException("Пользователь с такой электронной почтой уже существует");
        }
    }
}
