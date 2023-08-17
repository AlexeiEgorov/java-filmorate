package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FutureBirthDateException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.exception.NoAtSignSymbolInEmailException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Set<User> users = new HashSet<>();
    private int nextUserId = 1;

    @PostMapping
    @ResponseBody
    public User addUser(@Valid @RequestBody User user) {
        if (!user.getEmail().contains("@")) {
            log.debug("Клиент пытается зарегестрироваться используя невалидный email: {}", user.getEmail());
            throw new NoAtSignSymbolInEmailException();
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Клиент пытается зарегестрироваться используя невалидный логин: {}", user.getLogin());
            throw new InvalidLoginException();
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Клиент пытается зарегестрироваться используя будущую дату рождения: {}",
                    user.getBirthday());
            throw new FutureBirthDateException();
        } else if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Клиент регестрируется не вводя имени, и оно замещается логином: {}", user.getLogin());
            user = user.toBuilder().name(user.getLogin()).build();
        }
        user.setId(nextUserId++);
        users.add(user);
        return user;
    }

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.contains(user)) {
            log.debug("Клиент пытается обновить пользователя с несуществующим id: {}", user.getId());
            throw new NoSuchUserException();
        } else if (!user.getEmail().contains("@")) {
            log.debug("Клиент пытается обновить пользователя используя невалидный email: {}", user.getEmail());
            throw new NoAtSignSymbolInEmailException();
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Клиент пытается обновить пользователя используя невалидный логин: {}", user.getLogin());
            throw new InvalidLoginException();
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Клиент пытается обновить пользователя используя будущую дату рождения: {}", user.getBirthday());
            throw new FutureBirthDateException();
        } else if (user.getName().isBlank()) {
            log.debug("Клиент пытается обновить пользователя не вводя имени, и оно замещается логином: {}",
                    user.getLogin());
            user = user.toBuilder().name(user.getLogin()).build();
        }
        users.remove(user);
        users.add(user);
        return user;
    }

    @GetMapping
    @ResponseBody
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
}
