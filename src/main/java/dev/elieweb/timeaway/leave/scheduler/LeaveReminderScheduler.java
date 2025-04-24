package dev.elieweb.timeaway.leave.scheduler;

import dev.elieweb.timeaway.email.service.EmailService;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LeaveReminderScheduler {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *") // Run at 8 AM every day
    public void sendUpcomingLeaveReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<LeaveRequest> upcomingLeaves = leaveRequestRepository
                .findByStatusAndStartDate(LeaveStatus.APPROVED, tomorrow);

        for (LeaveRequest leaveRequest : upcomingLeaves) {
            emailService.sendUpcomingLeaveReminder(leaveRequest);
        }
    }
} 