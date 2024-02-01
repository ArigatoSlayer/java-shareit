package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
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
        checkEmailForCreate(user);
        user.setId(++id);
        userMap.put(user.getId(), user);
        log.info("Создан пользователь с id = {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        checkUser(userId);
        User originalUserCopy = userMap.get(userId);
        if (user.getName() != null) originalUserCopy.setName(user.getName());
        if (user.getEmail() != null) {
            checkEmailForUpdate(user, userId);
            originalUserCopy.setEmail(user.getEmail());
        }
        userMap.put(userId, originalUserCopy);
        return UserMapper.toUserDto(userMap.get(userId));
    }

    @Override
    public UserDto getUserById(Long id) {
        checkUser(id);
        log.info("Получен пользователь с id = {}", id);
        return UserMapper.toUserDto(userMap.get(id));
    }

    @Override
    public void deleteUserById(Long id) {
        checkUser(id);
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

    private void checkEmailForCreate(User user) {
        for (User originalUser : userMap.values()) {
            if (user.getEmail().equals(originalUser.getEmail())) {
                throw new NotValidException(String.format("пользователь с таким Email = %s уже существует", user.getEmail()));
            }
        }
    }

    private void checkEmailForUpdate(User user, Long id) {
        for (User originalUser : userMap.values()) {
            if (user.getEmail().equals(originalUser.getEmail())) {
                if (!originalUser.getId().equals(id)) {
                    throw new NotValidException(String.format("пользователь с таким Email = %s уже существует", user.getEmail()));
                }
            }
        }
    }

    private void checkUser(Long id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException(String.format("id = %s не найдено", id));
        }
    }
}
