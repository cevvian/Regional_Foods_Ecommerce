package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.NotificationMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.NotificationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NotificationResponse;
import edu.ut.sales.sales_analyst.model.entities.Notification;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.repositories.NotificationRepo;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
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
    private final UserRepo userRepo;
    private final UserService userService;

    public NotificationService(NotificationMapper notificationMapper, NotificationRepo notificationRepo, UserRepo userRepo, UserService userService) {
        this.notificationMapper = notificationMapper;
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Override
    public Page<NotificationResponse> getByUserIdAndIsRead(Pageable pageable, Boolean isRead) {
        User currentUser = userService.getCurrentUser();
        Page<Notification> notifications;

        notifications = notificationRepo.findByUserIdAndIsReadOptional(pageable, currentUser.getUserId(), isRead);

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
    public Long countUnread() {
        User currentUser = userService.getCurrentUser();
        return notificationRepo.countByUserIdAndIsReadNot(currentUser.getUserId());
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        Boolean notification = notificationRepo.existsNotificationByContentAndTitle(request.getTitle(), request.getMessage());
        if (notification) {
            throw new AppException(ErrorCode.NOTIFICATION_ALREADY_EXISTS);
        }

        User user = userRepo.findByUserId(request.getUserId());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Notification notificationEntity = new Notification();
        notificationEntity.setTitle(request.getTitle());
        notificationEntity.setContent(request.getMessage());
        notificationEntity.setUser(user);
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
    public Boolean markAllIsRead() {
        User currentUser = userService.getCurrentUser();

        User user = userRepo.findByUserId(currentUser.getUserId());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        List<Notification> unreadNotifications = notificationRepo.findByUserIdAndIsReadNot(user.getUserId());

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
    public Boolean deleteAllByUser() {
        User currentUser = userService.getCurrentUser();
        List<Notification> userNotifications = notificationRepo.findByUserId(currentUser.getUserId());

        if (userNotifications.isEmpty()) {
            throw new AppException(ErrorCode.NOTIFICATION_LIST_EMPTY);
        }

        notificationRepo.deleteAll(userNotifications);
        return true;
    }
}
