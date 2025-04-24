package dev.elieweb.timeaway.email.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.email.provider", havingValue = "mailjet")
public class MailjetEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(MailjetEmailService.class);

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    @Override
    @Async
    public void sendLeaveRequestSubmittedEmail(LeaveRequest leaveRequest) {
        log.info("Preparing to send leave request submitted email for user: {}", leaveRequest.getUser().getEmail());
        String subject = "Leave Request Submitted";
        String content = String.format(
            "Your leave request has been submitted successfully.\n\n" +
            "Type: %s\n" +
            "Start Date: %s\n" +
            "End Date: %s\n" +
            "Status: Pending\n\n" +
            "You will be notified when your request is reviewed.",
            leaveRequest.getType(),
            leaveRequest.getStartDate(),
            leaveRequest.getEndDate()
        );

        try {
            sendEmail(leaveRequest.getUser().getEmail(), subject, content, false);
            log.info("Leave request submitted email sent successfully to: {}", leaveRequest.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send leave request submitted email to {}: {}", leaveRequest.getUser().getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send leave request submitted email", e);
        }
    }

    @Override
    @Async
    public void sendLeaveRequestStatusUpdateEmail(LeaveRequest leaveRequest) {
        log.info("Preparing to send leave request status update email for user: {}", leaveRequest.getUser().getEmail());
        String subject = "Leave Request Status Update";
        String content = String.format(
            "Your leave request status has been updated.\n\n" +
            "Type: %s\n" +
            "Start Date: %s\n" +
            "End Date: %s\n" +
            "Status: %s\n" +
            "%s",
            leaveRequest.getType(),
            leaveRequest.getStartDate(),
            leaveRequest.getEndDate(),
            leaveRequest.getStatus(),
            leaveRequest.getRejectionReason() != null ? "\nRejection Reason: " + leaveRequest.getRejectionReason() : ""
        );

        try {
            sendEmail(leaveRequest.getUser().getEmail(), subject, content, false);
            log.info("Leave request status update email sent successfully to: {}", leaveRequest.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send leave request status update email to {}: {}", leaveRequest.getUser().getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send leave request status update email", e);
        }
    }

    @Override
    @Async
    public void sendUpcomingLeaveReminder(LeaveRequest leaveRequest) {
        log.info("Preparing to send upcoming leave reminder email for user: {}", leaveRequest.getUser().getEmail());
        String subject = "Upcoming Leave Reminder";
        String content = String.format(
            "This is a reminder that your leave starts tomorrow.\n\n" +
            "Type: %s\n" +
            "Start Date: %s\n" +
            "End Date: %s\n",
            leaveRequest.getType(),
            leaveRequest.getStartDate(),
            leaveRequest.getEndDate()
        );

        try {
            sendEmail(leaveRequest.getUser().getEmail(), subject, content, false);
            log.info("Upcoming leave reminder email sent successfully to: {}", leaveRequest.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send upcoming leave reminder email to {}: {}", leaveRequest.getUser().getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send upcoming leave reminder email", e);
        }
    }

    @Override
    @Async
    public void sendWelcomeEmail(User user) {
        log.info("Preparing to send welcome email to: {}", user.getEmail());
        String subject = "Welcome to TimeAway";
        String content = String.format(
            "Welcome to TimeAway, %s!\n\n" +
            "Your account has been successfully created. You can now start managing your leave requests.\n\n" +
            "Best regards,\n" +
            "The TimeAway Team",
            user.getFirstName()
        );

        try {
            sendEmail(user.getEmail(), subject, content, false);
            log.info("Welcome email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", user.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    private void sendEmail(String to, String subject, String content, boolean isHtml) throws Exception {
        log.debug("Initializing MailJet client with API key: {}...", apiKey.substring(0, 4));
        MailjetClient client = new MailjetClient(ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecret)
                .build());

        JSONObject message = new JSONObject()
                .put("From", new JSONObject()
                        .put("Email", fromEmail)
                        .put("Name", fromName))
                .put("To", new JSONArray()
                        .put(new JSONObject()
                                .put("Email", to)))
                .put("Subject", subject)
                .put(isHtml ? "HTMLPart" : "TextPart", content);

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray().put(message));

        log.debug("Sending email to {} with subject: {}", to, subject);
        MailjetResponse response = client.post(request);
        
        if (response.getStatus() != 200) {
            log.error("Failed to send email. Status: {}, Error: {}", response.getStatus(), response.getData());
            throw new Exception("Failed to send email: " + response.getData());
        }
        
        log.debug("Email sent successfully. Response status: {}", response.getStatus());
    }
} 