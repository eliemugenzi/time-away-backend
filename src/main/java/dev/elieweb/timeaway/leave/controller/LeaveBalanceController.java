package dev.elieweb.timeaway.leave.controller;

import dev.elieweb.timeaway.auth.service.CurrentUserService;
import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import dev.elieweb.timeaway.leave.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/leave-balances")
@RequiredArgsConstructor
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;
    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<LeaveType, Integer>>> getCurrentUserBalances() {
        Map<LeaveType, Integer> balances = leaveBalanceService.getCurrentBalances(currentUserService.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Leave balances retrieved successfully", balances));
    }
} 