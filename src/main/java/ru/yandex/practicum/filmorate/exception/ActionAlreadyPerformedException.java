package ru.yandex.practicum.filmorate.exception;

public class ActionAlreadyPerformedException extends RuntimeException {
    public ActionAlreadyPerformedException(String message) {
        super(message);
    }
}
