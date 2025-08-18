package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.NotificationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INotificationService {
    Page<NotificationResponse> getByUserIdAndIsRead(Pageable pageable, Boolean isRead);
    NotificationResponse getDetail(String notificationId);
    Long countUnread();

    NotificationResponse createNotification(NotificationRequest request);

    Boolean markOneIsRead(String notificationId);
    Boolean markAllIsRead();

    Boolean deleteNotification(String notificationId);
    Boolean deleteAllByUser();
}

