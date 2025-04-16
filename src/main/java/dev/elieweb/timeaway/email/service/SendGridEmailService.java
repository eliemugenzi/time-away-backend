package dev.elieweb.timeaway.email.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendGridEmailService {
    private final SendGrid sendGrid;
    private final TemplateEngine templateEngine;

    @Value("${spring.sendgrid.from-email}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String templateName, Context context) throws IOException {
        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        String htmlContent = templateEngine.process(templateName, context);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        if (response.getStatusCode() >= 400) {
            throw new IOException("Failed to send email: " + response.getBody());
        }
    }

    public void sendWelcomeEmail(User user) throws IOException {
        Context context = new Context();
        context.setVariable("name", user.getFirstName());

        sendEmail(
            user.getEmail(),
            "Welcome to TimeAway",
            "welcome",
            context
        );
    }

    public void sendLeaveRequestNotification(LeaveRequest leaveRequest) throws IOException {
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeName", leaveRequest.getUser().getFirstName());
        variables.put("startDate", leaveRequest.getStartDate());
        variables.put("endDate", leaveRequest.getEndDate());
        variables.put("type", leaveRequest.getType());
        variables.put("reason", leaveRequest.getReason());
        context.setVariables(variables);

        sendEmail(
            leaveRequest.getApprover().getEmail(),
            "New Leave Request",
            "leave-request",
            context
        );
    }

    public void sendLeaveStatusUpdateNotification(LeaveRequest leaveRequest) throws IOException {
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeName", leaveRequest.getUser().getFirstName());
        variables.put("status", leaveRequest.getStatus());
        variables.put("startDate", leaveRequest.getStartDate());
        variables.put("endDate", leaveRequest.getEndDate());
        variables.put("type", leaveRequest.getType());
        if (leaveRequest.getRejectionReason() != null) {
            variables.put("rejectionReason", leaveRequest.getRejectionReason());
        }
        context.setVariables(variables);

        sendEmail(
            leaveRequest.getUser().getEmail(),
            "Leave Request Status Update",
            "leave-status-update",
            context
        );
    }
} 