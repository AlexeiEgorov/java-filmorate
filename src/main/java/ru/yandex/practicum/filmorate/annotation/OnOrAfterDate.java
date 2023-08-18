package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OnOrAfterDateConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface OnOrAfterDate {
    String value();
    String message() default "{AfterDate}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
