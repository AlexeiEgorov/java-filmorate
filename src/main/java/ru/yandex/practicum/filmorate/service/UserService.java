package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
        return userStorage.validateAndGetUserIfRegistered(id);
    }

    public void addFriend(int userId, int friendId) {
        final User user = userStorage.validateAndGetUserIfRegistered(userId);
        final User friend = userStorage.validateAndGetUserIfRegistered(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void deleteFriend(int userId, int friendId) {
        final User user = userStorage.validateAndGetUserIfRegistered(userId);
        userStorage.validateUserRegistration(friendId);
        user.getFriends().remove(friendId);
    }

    public List<User> getCommonFriends(int firstUserId, int secUserId) {
        final User user1 = userStorage.validateAndGetUserIfRegistered(firstUserId);
        final User user2 = userStorage.validateAndGetUserIfRegistered(secUserId);
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
                commonFriends.add(userStorage.getUser(friendId));
            }
        }

        return commonFriends;
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }
}
