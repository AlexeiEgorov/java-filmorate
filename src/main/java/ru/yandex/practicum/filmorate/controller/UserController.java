package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

import static ru.yandex.practicum.filmorate.Constants.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (isUserNameEmpty(user)) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validateId(ID, user.getId());
        if (isUserNameEmpty(user)) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        validateId(ID, id);
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        //validateId(ID, id);
        //validateId(FRIEND_ID, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        validateId(ID, id);
        validateId(FRIEND_ID, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        validateId(ID, id);
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        validateId(ID, id);
        validateId(OTHER_ID, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    private static boolean isUserNameEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Клиент регестрируется не вводя имени, и оно замещается логином: {}", user.getLogin());
            return true;
        }
        return false;
    }

    static void validateId(String param, int id) {
        if (id < 1) {
            throw new ValidationException(param, id);
        }
    }
}
