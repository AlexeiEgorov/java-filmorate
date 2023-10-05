package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.OnOrAfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Film {
    public static final String FIRST_FILM_RELEASE = "1895-12-28";
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private final String name;
    @NotNull(message = "Описание должно быть передано")
    @Size(max = 200, message = "Длина описания не может превышать 200 символов")
    private final String description;
    @NotNull
    @OnOrAfterDate(value = FIRST_FILM_RELEASE, message = "Дата выпуска фильма не может быть раньше " +
            FIRST_FILM_RELEASE)
    private final LocalDate releaseDate;
    @Positive(message = "Длительность не может быть неположительной")
    private final int duration;
    @JsonIgnore
    private Set<Integer> usersWhoLiked = new HashSet<>();
    private List<Genre> genres = new ArrayList<>();
    private Mpa mpa;

    @JsonIgnore
    public int getLikes() {
        return usersWhoLiked.size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        if (mpa == null) {
            values.put("mpa_id", null);
        } else {
            values.put("mpa_id", mpa.getId());
        }
        return values;
    }
}
