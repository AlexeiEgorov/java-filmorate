package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.USER;

@Repository("inMemoryUserStorage")
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
    public Optional<User> findUser(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getFriends(int userId) {
        return findUser(userId).orElseThrow(() -> new EntityNotFoundException(USER, userId)).getFriends().stream()
                .map(users::get).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        final User user = findUser(userId).orElseThrow(() -> new EntityNotFoundException(USER, userId));
        final User friend = findUser(friendId).orElseThrow(() -> new EntityNotFoundException(USER, friendId));
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        validateUserRegistration(friendId);
        findUser(userId).orElseThrow(() -> new EntityNotFoundException(USER, userId)).getFriends().remove(friendId);
    }

    @Override
    public List<User> getCommonFriends(int firstUserId, int secUserId) {
        final User user1 = findUser(firstUserId).orElseThrow(() -> new EntityNotFoundException(USER, firstUserId));
        final User user2 = findUser(secUserId).orElseThrow(() -> new EntityNotFoundException(USER, secUserId));
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
                commonFriends.add(findUser(friendId).get());
            }
        }

        return commonFriends;
    }

    //@Override
    public void validateUserRegistration(int userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException(USER, userId);
        }
    }
}
