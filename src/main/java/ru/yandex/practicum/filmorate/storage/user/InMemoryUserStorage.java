package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.USER;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private int nextUserId;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
        this.nextUserId = 1;
    }

    @Override
    public User addUser(User user) {
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUserRegistration(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUser(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getFriends(int userId) {
        return getUser(userId).orElseThrow(() -> new EntityNotFoundException(USER, userId)).getFriends().stream()
                .map(users::get).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void validateUserRegistration(int userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException(USER, userId);
        }
    }
}
