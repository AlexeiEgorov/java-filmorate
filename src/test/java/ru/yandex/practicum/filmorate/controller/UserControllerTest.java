package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController userController;
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void addUser() {
        final User user1 = new User("emailcom", "login", "Isaac",
                LocalDate.of(2000, 1, 1));
        userController.addUser(user1);
        final Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        assertEquals(1, violations1.size(), "Контроллер добавил пользователя с email без адресного " +
                "знака");

        final User user2 = new User("isaac@gmail.com", "log   in", "Isaac",
                LocalDate.of(2001, 1, 1));
        userController.addUser(user2);
        final Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        assertEquals(1, violations2.size(), "Контроллер добавил пользователя с логином содержащим " +
                "пробелы");

        final User user3 = new User("isaac@gmail.com", "", "Isaac", LocalDate.of(2001, 1,
                1));
        userController.addUser(user3);
        final Set<ConstraintViolation<User>> violations3 = validator.validate(user3);
        assertEquals(2, violations3.size(), "Контроллер добавил пользователя с пустым логином");

        final User user4 = new User("isaac@gmail.com", "Caleb", "",
                LocalDate.of(2001, 1, 1));
        assertEquals("Caleb", userController.addUser(user4).getName(), "Контроллер" +
                " добавил пользователя без имени и не установил его равным логину");

        final User user5 = new User("isaac@gmail.com", "Caleb", "",
                LocalDate.now().plusDays(1));
        userController.addUser(user5);
        final Set<ConstraintViolation<User>> violations5 = validator.validate(user5);
        assertEquals(1, violations5.size(), "Контроллер добавил пользователя с будущим днём рождения");
    }

    @Test
    void updateUser() {
        userController.addUser(new User("@emailcom", "login", "Isaac",
                LocalDate.of(2000, 1, 1)));

        final User user2 = new User(2, "@emailcom", "Caleb", "",
                LocalDate.now().minusYears(1));
        assertThrows(EntityNotFoundException.class, () -> userController.updateUser(user2), "Контроллер " +
                "пропустил фильм с незарегестрированным id");
    }

    @Test
    void getUsers() {
        final User user1 = new User("@emailcom", "login", "Isaac",
                LocalDate.of(2000, 1, 1));
        final User user2 = new User("@emailcom", "login", "Redrick",
                LocalDate.of(2000, 1, 1));
        final User user3 = new User("@emailcom", "login", "Vitaliy",
                LocalDate.of(2000, 1, 1));
        final User newUser3 = new User(3, "@emailcom", "EvilArthas", "Vitaliy",
                LocalDate.of(2000, 1, 1));
        userController.addUser(user1);
        userController.addUser(user2);
        userController.addUser(user3);
        userController.updateUser(newUser3);

        assertEquals(List.of(user1, user2, newUser3), userController.getUsers());
    }
}
