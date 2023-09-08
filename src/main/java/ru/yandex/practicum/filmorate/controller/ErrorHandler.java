package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import static ru.yandex.practicum.filmorate.Constants.*;

@RestControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        if (ID_TYPES.contains(e.getParameter())) {
            return new ErrorResponse(String.format("параметр (%s); значение (%s)", e.getParameter(), e.getValue()),
                    "Параметр id не может быть непозитивным числом");
        } else if (COUNT.equals(e.getParameter())) {
            return new ErrorResponse(String.format("параметр (%s); значение (%s)", e.getParameter(), e.getValue()),
                    "Параметр count не может быть непозитивным числом");
        }
        return new ErrorResponse((String.format("параметр (%s); значение (%s)", e.getParameter(), e.getValue())),
                "неизвестная ошибка");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        if (USER.equals(e.getEntityClass())) {
            return new ErrorResponse(String.format("id - (%s)", e.getValue()),
                    "Пользователь с данным id, не зарегестрирован");
        } else if (FILM.equals(e.getEntityClass())) {
            return new ErrorResponse(String.format("id - (%s)", e.getValue()),
                    "Фильм с данным id, не зарегестрирован");
        }
        return new ErrorResponse((String.format("объект (%s); значение (%s)", e.getEntityClass(), e.getValue())),
                "неизвестная ошибка");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedError(Throwable e) {
        return new ErrorResponse(e.getMessage(), "возникла непредвиденная ошибка");
    }
}
