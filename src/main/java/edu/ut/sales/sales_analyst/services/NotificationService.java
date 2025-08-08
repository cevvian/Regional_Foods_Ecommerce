package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.NotificationMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.NotificationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NotificationResponse;
import edu.ut.sales.sales_analyst.model.entities.Notification;
import edu.ut.sales.sales_analyst.repositories.NotificationRepo;
import edu.ut.sales.sales_analyst.services.impl.INotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService implements INotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationRepo notificationRepo;

    public NotificationService(NotificationMapper notificationMapper, NotificationRepo notificationRepo) {
        this.notificationMapper = notificationMapper;
        this.notificationRepo = notificationRepo;
    }

    @Override
    public Page<NotificationResponse> getByUserIdAndIsRead(Pageable pageable, String userId, Boolean isRead) {
        Page<Notification> notifications;

        notifications = notificationRepo.findByUserIdAndIsReadOptional(pageable, userId, isRead);

        if (notifications.isEmpty()) {
            throw new AppException(ErrorCode.NOTIFICATION_LIST_EMPTY);
        }

        return notifications.map(notificationMapper::toNotificationResponse);
    }

    @Override
    public NotificationResponse getDetail(String notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public Long countUnread(String userId) {

        return notificationRepo.countByUserIdAndIsReadNot(userId);
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        Boolean notification = notificationRepo.existsNotificationByContentAndTitle(request.getTitle(), request.getMessage());
        if (notification) {
            throw new AppException(ErrorCode.NOTIFICATION_ALREADY_EXISTS);
        }
        Notification notificationEntity = new Notification();
        notificationEntity.setTitle(request.getTitle());
        notificationEntity.setContent(request.getMessage());
        notificationRepo.save(notificationEntity);
        return notificationMapper.toNotificationResponse(notificationEntity);
    }

    @Override
    public Boolean markOneIsRead(String notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (Boolean.TRUE.equals(notification.getIsRead())) {
            return true;
        }

        notification.setIsRead(true);
        notificationRepo.save(notification);
        return true;
    }


    @Override
    @Transactional
    public Boolean markAllIsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepo.findByUserIdAndIsReadNot(userId);

        if (unreadNotifications.isEmpty()) {
            throw new AppException(ErrorCode.NOTIFICATION_LIST_EMPTY);
        }

        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
        }

        notificationRepo.saveAll(unreadNotifications);
        return true;
    }

    @Override
    public Boolean deleteNotification(String notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notificationRepo.delete(notification);
        return true;
    }

    @Override
    public Boolean deleteAllByUser(String userId) {
        List<Notification> userNotifications = notificationRepo.findByUserId(userId);

        if (userNotifications.isEmpty()) {
            throw new AppException(ErrorCode.NOTIFICATION_LIST_EMPTY);
        }

        notificationRepo.deleteAll(userNotifications);
        return true;
    }
}
