package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.USER;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id).orElseThrow(() -> new EntityNotFoundException(USER, id));
    }

    public void addFriend(int userId, int friendId) {
        final User user = getUser(userId);
        final User friend = getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.validateUserRegistration(friendId);
        getUser(userId).getFriends().remove(friendId);
    }

    public List<User> getCommonFriends(int firstUserId, int secUserId) {
        final User user1 = getUser(firstUserId);
        final User user2 = getUser(secUserId);
        final List<User> commonFriends = new ArrayList<>();
        User fewerFriendsUser;
        User moreFriendsUser;

        if (user1.getFriends().size() <= user2.getFriends().size()) {
            fewerFriendsUser = user1;
            moreFriendsUser = user2;
        } else {
            fewerFriendsUser = user2;
            moreFriendsUser = user1;
        }

        for (int friendId : fewerFriendsUser.getFriends()) {
            if (moreFriendsUser.getFriends().contains(friendId)) {
                commonFriends.add(userStorage.getUser(friendId).get());
            }
        }

        return commonFriends;
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }
}
