package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FutureBirthDateException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.exception.NoAtSignSymbolInEmailException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void addUser() {
        final User user1 = new User("emailcom", "login", "Isaac" ,
                LocalDate.of(2000, 1, 1));
        assertThrows(NoAtSignSymbolInEmailException.class, () -> userController.addUser(user1), "Контроллер" +
                " добавил пользователя с email без адресного знака");

        final User user2 = new User("@emailcom", "log in", "Isaac" ,
                LocalDate.of(2001, 1, 1));
        final User user3 = new User("@emailcom", "", "Isaac" ,
                LocalDate.of(2001, 1, 1));
        assertThrows(InvalidLoginException.class, () -> userController.addUser(user2), "Контроллер" +
                " добавил пользователя с логином содержащим пробелы");
        assertThrows(InvalidLoginException.class, () -> userController.addUser(user3), "Контроллер" +
                " добавил пользователя с пустым логином");

        final User user4 = new User("@emailcom", "Caleb", "" ,
                LocalDate.of(2001, 1, 1));
        assertEquals("Caleb", userController.addUser(user4).getName(), "Контроллер" +
                " добавил пользователя без имени и не установил его равным логину");

        final User user5 = new User("@emailcom", "Caleb", "" ,
                LocalDate.now().plusDays(1));
        assertThrows(FutureBirthDateException.class, () -> userController.addUser(user5), "Контроллер" +
                " добавил пользователя с будущим днём рождения");
    }

    @Test
    void updateUser() {
        userController.addUser(new User("@emailcom", "login", "Isaac" ,
                LocalDate.of(2000, 1, 1)));

        final User user1 = new User(1, "emailcom", "login", "Isaac",
                LocalDate.of(2000, 1, 1));
        assertThrows(NoAtSignSymbolInEmailException.class, () -> userController.updateUser(user1),
                "Контроллер обновил пользователя используя новую версию, с email без адресного знака");

        final User user2 = new User(1, "@emailcom", "log in", "Isaac" ,
                LocalDate.of(2001, 1, 1));
        final User user3 = new User(1, "@emailcom", "", "Isaac" ,
                LocalDate.of(2001, 1, 1));
        assertThrows(InvalidLoginException.class, () -> userController.updateUser(user2), "Контроллер" +
                " обновил пользователя используя новую версию, с логином содержащим пробелы");
        assertThrows(InvalidLoginException.class, () -> userController.updateUser(user3), "Контроллер" +
                " обновил пользователя используя новую версию, с пустым логином");

        final User user4 = new User(1, "@emailcom", "Caleb", "" ,
                LocalDate.of(2001, 1, 1));
        assertEquals("Caleb", userController.updateUser(user4).getName(), "Контроллер" +
                " обновил пользователя используя новую версию, без имени и не установил его равным логину");

        final User user5 = new User(1, "@emailcom", "Caleb", "" ,
                LocalDate.now().plusDays(1));
        assertThrows(FutureBirthDateException.class, () -> userController.updateUser(user5), "Контроллер" +
                " обновил пользователя используя новую версию, с будущим днём рождения");
    }

    @Test
    void getUsers() {
        final User user1 = new User("@emailcom", "login", "Isaac" ,
                LocalDate.of(2000, 1, 1));
        final User user2 = new User("@emailcom", "login", "Redrick" ,
                LocalDate.of(2000, 1, 1));
        final User user3 = new User("@emailcom", "login", "Vitaliy" ,
                LocalDate.of(2000, 1, 1));
        final User newUser3 = new User(3, "@emailcom", "EvilArthas", "Vitaliy" ,
                LocalDate.of(2000, 1, 1));
        userController.addUser(user1);
        userController.addUser(user2);
        userController.addUser(user3);
        userController.updateUser(newUser3);

        assertEquals(List.of(user1, user2, newUser3), userController.getUsers());
    }
}
