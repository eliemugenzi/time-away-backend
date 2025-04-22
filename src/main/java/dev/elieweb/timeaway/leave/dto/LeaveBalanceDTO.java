package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDTO {
    @NotNull(message = "User is required")
    private User user;

    @NotNull(message = "Leave type is required")
    private LeaveType type;

    @NotNull(message = "Year is required")
    @Min(value = 2024, message = "Year must be 2024 or later")
    private Integer year;

    @NotNull(message = "Total days is required")
    @Min(value = 1, message = "Total days must be at least 1")
    private Integer totalDays;
} 