package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id"})
public class User {
    private int id;
    @NotNull(message = "email должен пыть передан")
    @Email(message = "email должен быть валиден")
    private final String email;
    @NotNull(message = "логин должен быть передан")
    @NotBlank(message = "логин не может быть пустым")
    private final String login;
    private final String name;
    @PastOrPresent(message = "дата рождения не может быть будущей")
    private final LocalDate birthday;
}
