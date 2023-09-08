package ru.yandex.practicum.filmorate.exception;

public class ImpossibleToUndoUnperformedActionException extends RuntimeException {
    public ImpossibleToUndoUnperformedActionException(String message) {
        super(message);
    }
}
