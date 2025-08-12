package edu.ut.sales.sales_analyst.consumer;

import edu.ut.sales.sales_analyst.model.dtos.events.PasswordChangedEvent;
import edu.ut.sales.sales_analyst.model.dtos.requests.NotificationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NotificationResponse;
import edu.ut.sales.sales_analyst.services.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PasswordChangedConsumer {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public PasswordChangedConsumer(NotificationService notificationService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(
            topics = "user.password.changed",
            groupId = "notification-group",
            containerFactory = "passwordChangedKafkaListenerContainerFactory"
    )
    public void handlePasswordChanged(PasswordChangedEvent event) {
        // Lưu notification vào DB
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(event.getUserId());
        notificationRequest.setTitle("Reset Password");
        LocalDateTime changedAt = event.getChangedAt();

        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime vietnamTime = changedAt.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(vietnamZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = vietnamTime.format(formatter);

        notificationRequest.setMessage("Your password has been reset at " + formattedDateTime);
        NotificationResponse notification = notificationService.createNotification(notificationRequest);

        messagingTemplate.convertAndSend("/queue/notifications-" + event.getUserId(), notification);
    }
}