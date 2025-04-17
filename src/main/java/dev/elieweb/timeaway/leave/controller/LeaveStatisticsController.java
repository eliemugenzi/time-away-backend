package dev.elieweb.timeaway.leave.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.leave.dto.DepartmentStatistics;
import dev.elieweb.timeaway.leave.dto.LeaveStatistics;
import dev.elieweb.timeaway.leave.service.LeaveStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-statistics")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class LeaveStatisticsController {
    private final LeaveStatisticsService leaveStatisticsService;

    @GetMapping("/overall")
    public ResponseEntity<ApiResponse> getOverallStatistics() {
        try {
            LeaveStatistics statistics = leaveStatisticsService.getOverallStatistics();
            return ResponseEntity.ok(ApiResponse.success("Overall leave statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to retrieve overall leave statistics"));
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse> getDepartmentStatistics() {
        List<DepartmentStatistics> statistics = leaveStatisticsService.getDepartmentStatistics();
        return ResponseEntity.ok(ApiResponse.success("Department leave statistics retrieved successfully", statistics));
    }
} 