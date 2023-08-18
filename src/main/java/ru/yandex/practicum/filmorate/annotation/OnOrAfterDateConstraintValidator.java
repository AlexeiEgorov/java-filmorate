package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class OnOrAfterDateConstraintValidator implements ConstraintValidator<OnOrAfterDate, LocalDate> {
    private LocalDate annotationDate;

    @Override
    public void initialize(OnOrAfterDate onOrAfterDate) {
        this.annotationDate = LocalDate.parse(onOrAfterDate.value());
    }

    @Override
    public boolean isValid(LocalDate target, ConstraintValidatorContext cxt) {
        if (target == null) {
            return false;
        }
        return target.isAfter(annotationDate) || target.isEqual(annotationDate);
    }
}
