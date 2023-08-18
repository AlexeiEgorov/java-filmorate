package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.OnOrAfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id"})
public class Film {
    public static final String firstFilmRelease = "1895-12-28";
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private final String name;
    @NotNull(message = "Описание должно быть передано")
    @Size(max = 200, message = "Длина описания не может превышать 200 символов")
    private final String description;
    @OnOrAfterDate(value = firstFilmRelease, message = "Дата выпуска фильма не может быть раньше " + firstFilmRelease)
    private final LocalDate releaseDate;
    @Positive(message = "Длительность не может быть неположительной")
    private final int duration;
}
