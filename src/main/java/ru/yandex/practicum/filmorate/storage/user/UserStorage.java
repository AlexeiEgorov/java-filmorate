package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    Optional<User> getUser(int id);

    List<User> getFriends(int userId);

    void validateUserRegistration(int userId);
}
