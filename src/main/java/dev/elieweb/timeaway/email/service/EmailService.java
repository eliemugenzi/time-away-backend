package dev.elieweb.timeaway.email.service;

import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.auth.entity.User;

public interface EmailService {
    void sendLeaveRequestSubmittedEmail(LeaveRequest leaveRequest);
    void sendLeaveRequestStatusUpdateEmail(LeaveRequest leaveRequest);
    void sendUpcomingLeaveReminder(LeaveRequest leaveRequest);
    void sendWelcomeEmail(User user) throws Exception;
} 