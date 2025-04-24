package dev.elieweb.timeaway.leave.validation;

import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.enums.LeaveDurationType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LeaveDurationValidator implements ConstraintValidator<ValidLeaveDuration, LeaveRequestDTO> {

    @Override
    public void initialize(ValidLeaveDuration constraintAnnotation) {
    }

    @Override
    public boolean isValid(LeaveRequestDTO request, ConstraintValidatorContext context) {
        if (request == null || request.getStartDate() == null || request.getEndDate() == null || request.getDurationType() == null) {
            return true; // Let @NotNull handle these cases
        }

        // If dates are different, only FULL_DAY is allowed
        if (!request.getStartDate().isEqual(request.getEndDate())) {
            return request.getDurationType() == LeaveDurationType.FULL_DAY;
        }

        return true; // Same date can use either FULL_DAY or HALF_DAY
    }
} 