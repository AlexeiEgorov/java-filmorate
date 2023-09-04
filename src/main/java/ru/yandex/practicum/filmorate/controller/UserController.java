package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (isUserNameEmpty(user)) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) == null) {
            log.debug("Клиент пытается обновить пользователя с несуществующим id: {}", user.getId());
            throw new EntityNotFoundException(user.getClass().toString());
        }
        if (isUserNameEmpty(user)) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private static boolean isUserNameEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Клиент регестрируется не вводя имени, и оно замещается логином: {}", user.getLogin());
            return true;
        }
        return false;
    }
}
