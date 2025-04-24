package dev.elieweb.timeaway.leave.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LeaveDurationValidator.class)
@Documented
public @interface ValidLeaveDuration {
    String message() default "Half-day option is only available when start date equals end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 