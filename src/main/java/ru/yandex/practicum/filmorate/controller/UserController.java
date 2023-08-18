package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;

    @PostMapping
    @ResponseBody
    public User addUser(@Valid @RequestBody User user) {
        if (!user.getEmail().contains("@")) {
            log.debug("Клиент пытается зарегестрироваться используя невалидный email: {}", user.getEmail());
            throw new ValidationException("Невалидный email");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Клиент пытается зарегестрироваться используя невалидный логин: {}", user.getLogin());
            throw new ValidationException("Невалидный логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Клиент пытается зарегестрироваться используя будущую дату рождения: {}",
                    user.getBirthday());
            throw new ValidationException("Дата рождения в будущем");
        } else if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Клиент регестрируется не вводя имени, и оно замещается логином: {}", user.getLogin());
            user = user.toBuilder().name(user.getLogin()).build();
        }
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) == null) {
            log.debug("Клиент пытается обновить пользователя с несуществующим id: {}", user.getId());
            throw new EntityNotFoundException(user.getClass().toString());
        } else if (!user.getEmail().contains("@")) {
            log.debug("Клиент пытается зарегестрироваться используя невалидный email: {}", user.getEmail());
            throw new ValidationException("Невалидный email");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Клиент пытается зарегестрироваться используя невалидный логин: {}", user.getLogin());
            throw new ValidationException("Невалидный логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Клиент пытается зарегестрироваться используя будущую дату рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения в будущем");
        } else if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Клиент регестрируется не вводя имени, и оно замещается логином: {}", user.getLogin());
            user = user.toBuilder().name(user.getLogin()).build();
        }
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    @ResponseBody
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
