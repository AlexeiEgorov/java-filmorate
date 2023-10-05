package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.filmorate.Constants.FILM;
import static ru.yandex.practicum.filmorate.Constants.USER;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final GenreDaoImpl genreDao;

	@Test
	public void oneTestToCheckThemAll() {
		//) add all needed
		User user1 = User.builder()
				.email("crystal@light.com")
				.login("Crystal")
				.name("")
				.birthday(LocalDate.now())
				.friends(null)
				.build();
		User user2 = User.builder()
				.email("crystal2@light.com")
				.login("Crystal2")
				.name("")
				.birthday(LocalDate.now())
				.build();
		User user3 = User.builder()
				.email("leapinglight@westeast.com")
				.login("Light")
				.name("")
				.birthday(LocalDate.now())
				.friends(null)
				.build();

		Film film1 = Film.builder()
				.name("Matrix")
				.description("")
				.releaseDate(LocalDate.now())
				.duration(1)
				.usersWhoLiked(null)
				.genres(List.of(
						Genre.builder()
							.id(5)
							.name("Документальный")
							.build(),
						Genre.builder()
							.id(2)
							.name("Драма")
							.build()))
				.mpa(Mpa.builder()
						.id(1)
						.name("G")
						.build())
				.build();
		Film film2 = Film.builder()
				.name("The diagnosis and lectures of Ivan Pavlov")
				.description("")
				.releaseDate(LocalDate.now())
				.duration(1)
				.usersWhoLiked(null)
				.genres(List.of(
						Genre.builder()
								.id(5)
								.name("Документальный")
								.build(),
						Genre.builder()
								.id(4)
								.name("Триллер")
								.build()))
				.mpa(Mpa.builder()
						.id(2)
						.name("PG")
						.build())
				.build();
		userStorage.addUser(user1);
		userStorage.addUser(user2);
		userStorage.addUser(user3);
		filmStorage.addFilm(film1);
		filmStorage.addFilm(film2);
		user1.setId(1);
		user2.setId(2);
		user3.setId(3);
		film1.setId(1);
		film2.setId(2);

		// users get, update, friends
		Optional<User> userOptional = userStorage.findUser(1);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);

		List<User> users = List.of(user1, user2, user3);
		assertEquals(users, userStorage.getUsers());

		User userUpdated = user3.toBuilder().login("Wooster").build();
		userStorage.updateUser(userUpdated);
		assertEquals(userUpdated, userStorage.findUser(3).orElseThrow(() -> new EntityNotFoundException(USER, 3)));

		userStorage.addFriend(1, 2);
		userStorage.addFriend(1, 3);
		userStorage.addFriend(3, 1);
		userStorage.addFriend(3, 2);

		assertEquals(List.of(user1, user2), userStorage.getFriends(3));
		userStorage.deleteFriend(3, 1);
		assertEquals(List.of(user2), userStorage.getFriends(3));

		assertEquals(List.of(user2), userStorage.getCommonFriends(1, 3));

		// films get, update, likes
		Optional<Film> filmOptional = filmStorage.findFilm(1);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);

		List<Film> films = List.of(film1, film2);
		assertEquals(films, filmStorage.getFilms());

		List<Genre> genres = List.of(
				Genre.builder().id(5).name("Документальный").build(),
				Genre.builder().id(4).name("Триллер").build(),
				Genre.builder().id(2).name("Драма").build());

		assertEquals(genres.subList(0, 2), genreDao.getFilmGenres(2));

		Film filmUpdated = film2.toBuilder()
				.description("Must watch")
				.genres(genres)
				.build();
		filmStorage.updateFilm(filmUpdated);
		assertEquals(filmUpdated, filmStorage.findFilm(2).orElseThrow(() -> new EntityNotFoundException(FILM, 2)));
		assertEquals(genres, genreDao.getFilmGenres(2));

		filmStorage.likeFilm(3, 1);
		filmStorage.likeFilm(2, 2);
		filmStorage.likeFilm(1, 2);

		assertEquals(List.of(filmUpdated, film1), filmStorage.getMostLikedFilms(10));

		filmStorage.unlikeFilm(1, 2);
		filmStorage.unlikeFilm(2, 2);
		assertEquals(List.of(film1, filmUpdated), filmStorage.getMostLikedFilms(10));
	}


	/*
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final GenreDaoImpl genreDao;
	private final User user1;
	private final User user2;
	private final User user3;
	private final Film film1;
	private final Film film2;


	@Autowired
	public FilmoRateApplicationTests(UserDbStorage userStorage, FilmDbStorage filmStorage, GenreDaoImpl genreDao) {
		user1 = new User("crystal@light.com", "Crystal", "",
				LocalDate.of(192, 12, 12));
		user2 = new User("crystal2@light.com", "Crystal2", "",
				LocalDate.of(192, 12, 12));
		user3 = new User("leapinglight@westeast.com", "Light", "",
				LocalDate.of(192, 12, 12));
		film1 = Film.builder()
				.name("Matrix")
				.description("")
				.releaseDate(LocalDate.of(1997, 12, 27))
				.duration(1)
				.genres(List.of(Genre.builder().id(5).build(), Genre.builder().id(2).build()))
				.mpa(Mpa.builder().id(1).build())
				.build();
		film2 = Film.builder()
				.name("The diagnosis and lectures of Ivan Pavlov")
				.description("")
				.releaseDate(LocalDate.of(1927, 12, 27))
				.duration(1)
				.genres(List.of(Genre.builder().id(5).build(), Genre.builder().id(4).build()))
				.mpa(Mpa.builder().id(2).build())
				.build();
		userStorage.addUser(user1);
		userStorage.addUser(user2);
		userStorage.addUser(user3);
		filmStorage.addFilm(film1);
		filmStorage.addFilm(film2);
		user1.setId(1);
		user2.setId(2);
		user3.setId(3);
		film1.setId(1);
		film2.setId(2);
		this.userStorage = userStorage;
		this.filmStorage = filmStorage;
		this.genreDao = genreDao;
	}

	@Test
	public void testUserFriends() {
		userStorage.addFriend(1, 2);
		userStorage.addFriend(1, 3);
		userStorage.addFriend(3, 1);
		userStorage.addFriend(3, 2);

		assertEquals(List.of(user1, user2), userStorage.getFriends(3));
		userStorage.deleteFriend(3, 1);
		assertEquals(List.of(user2), userStorage.getFriends(3));

		assertEquals(List.of(user2), userStorage.getCommonFriends(1, 3));
	}

	@Test
	public void testGetAndUpdateUsers() {
		Optional<User> userOptional = userStorage.findUser(1);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);

		List<User> users = List.of(user1, user2, user3);
		assertEquals(users, userStorage.getUsers());

		User updated = user3.toBuilder().login("Wooster").build();
		userStorage.updateUser(updated);
		assertEquals(updated, userStorage.findUser(3).orElseThrow(() -> new EntityNotFoundException(USER, 3)));
	}

	@Test
	public void testGetAndUpdateAndGenresFilms() {
		Optional<Film> filmOptional = filmStorage.findFilm(1);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);

		List<Film> films = List.of(film1, film2);
		assertEquals(films, filmStorage.getFilms());

		List<Genre> genres = List.of(
				Genre.builder().id(5).name("Документальный").build(),
				Genre.builder().id(4).name("Триллер").build(),
				Genre.builder().id(2).name("Драма").build());

		assertEquals(genres.subList(0, 2), genreDao.getFilmGenres(2));

		Film updated = film2.toBuilder()
				.description("Must watch")
				.genres(genres)
				.build();
		filmStorage.updateFilm(updated);
		assertEquals(updated, filmStorage.findFilm(2).orElseThrow(() -> new EntityNotFoundException(FILM, 2)));
		assertEquals(genres, genreDao.getFilmGenres(2));
	}

	@Test
	void testFilmLikes() {
		filmStorage.likeFilm(3, 1);
		filmStorage.likeFilm(2, 2);
		filmStorage.likeFilm(1, 2);

		assertEquals(List.of(film2, film1), filmStorage.getMostLikedFilms(10));

		filmStorage.unlikeFilm(3, 1);
		assertEquals(List.of(film2), filmStorage.getMostLikedFilms(10));
	}
	 */
}
