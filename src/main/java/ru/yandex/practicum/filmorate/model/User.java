package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    private int id;
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "email должен быть валиден")
    private final String email;
    @NotBlank(message = "логин не может быть пустым")
    @Pattern(regexp = "^[^\\s]+$", message = "логин не может содержать пробелов")
    private final String login;
    private final String name;
    @NotNull
    @PastOrPresent(message = "дата рождения не может быть будущей")
    private final LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();

    public Map<String, Object> toMap() {
        final Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}
