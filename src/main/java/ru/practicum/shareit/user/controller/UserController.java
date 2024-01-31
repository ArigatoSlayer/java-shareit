package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid User user) {
        log.info("Запрос на создание пользователя");
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя по Id = {}", id);
        return userService.getUserById(id);
    }

    @RequestMapping(value = "/{id}",
    method = RequestMethod.PATCH)
    public UserDto update(@PathVariable Long id, @RequestBody User user) {
        log.info("Запрос на обновление пользователь пользователя c id = {}", id);
        return userService.updateUser(id, user);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя c id = {}", id);
        userService.deleteUserById(id);
    }
}