package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "email должен быть валиден")
    private final String email;
    @NotBlank(message = "логин не может быть пустым")
    private final String login;
    private final String name;
    @PastOrPresent(message = "дата рождения не может быть будущей")
    private final LocalDate birthday;
}
