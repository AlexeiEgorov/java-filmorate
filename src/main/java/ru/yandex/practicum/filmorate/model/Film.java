package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id"})
public class Film {
    private int id;
    @NotNull(message = "Имя должно быть передано")
    @NotBlank(message = "Имя не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Длина описания не может превышать 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "Длительность не может быть неположительной")
    private final int duration;
}
