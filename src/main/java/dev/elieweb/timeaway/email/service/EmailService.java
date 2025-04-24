package dev.elieweb.timeaway.email.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    @Async
    public void sendLeaveRequestNotification(LeaveRequest leaveRequest, String templateName) {
        try {
            User employee = leaveRequest.getUser();
            User manager = findManager(employee);

            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("employeeName", employee.getFirstName() + " " + employee.getLastName());
            variables.put("leaveType", leaveRequest.getType());
            variables.put("startDate", leaveRequest.getStartDate().format(DATE_FORMATTER));
            variables.put("endDate", leaveRequest.getEndDate().format(DATE_FORMATTER));
            variables.put("reason", leaveRequest.getReason());
            context.setVariables(variables);

            String htmlContent = templateEngine.process(templateName, context);
            sendEmail(manager.getEmail(), "Leave Request Notification", htmlContent, true);
        } catch (Exception e) {
            // Log the error but don't throw it to prevent transaction rollback
            e.printStackTrace();
        }
    }

    @Async
    public void sendLeaveStatusUpdateNotification(LeaveRequest leaveRequest) {
        try {
            User employee = leaveRequest.getUser();
            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("employeeName", employee.getFirstName());
            variables.put("status", leaveRequest.getStatus());
            variables.put("leaveType", leaveRequest.getType());
            variables.put("startDate", leaveRequest.getStartDate().format(DATE_FORMATTER));
            variables.put("endDate", leaveRequest.getEndDate().format(DATE_FORMATTER));
            if (leaveRequest.getRejectionReason() != null) {
                variables.put("rejectionReason", leaveRequest.getRejectionReason());
            }
            context.setVariables(variables);

            String templateName = "leave-status-update.html";
            String htmlContent = templateEngine.process(templateName, context);
            sendEmail(employee.getEmail(), "Leave Request Status Update", htmlContent, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String to, String subject, String content, boolean isHtml) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, isHtml);
        mailSender.send(message);
    }

    private User findManager(User employee) {
        // This should be implemented based on your organization structure
        // For now, we'll just find any manager in the same department
        return null; // Implement the logic to find the manager
    }

    public void sendWelcomeEmail(User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", user.getFirstName());

        String htmlContent = templateEngine.process("welcome", context);
        sendEmail(user.getEmail(), "Welcome to TimeAway", htmlContent, true);
    }
} 