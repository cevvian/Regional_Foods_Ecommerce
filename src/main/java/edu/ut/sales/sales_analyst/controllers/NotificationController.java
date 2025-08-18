package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.NotificationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NotificationResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/notifications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Notifications", description = "APIs for managing user notifications")
public class NotificationController {

    NotificationService notificationService;

    @Operation(summary = "Get notifications by userId and read status", description = "Retrieve notifications of a user filtered by read status (or all if null)")
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<List<NotificationResponse>> getNotificationsByUser(
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
            Pageable pageable = PageRequest.of(page, size);
            Page<NotificationResponse> notificationPage = notificationService.getByUserIdAndIsRead(pageable, isRead);

            PageMeta meta = PageMeta.builder()
                    .page(notificationPage.getNumber())
                    .size(notificationPage.getSize())
                    .totalElements(notificationPage.getTotalElements())
                    .totalPages(notificationPage.getTotalPages())
                    .last(notificationPage.isLast())
                    .build();

            return new ResponseAPI<>("Get notifications successfully", HttpStatus.OK, notificationPage.getContent(), meta);
    }

    @Operation(summary = "Get notification detail", description = "Retrieve detail of a specific notification")
    @GetMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<NotificationResponse> getDetail(@PathVariable String notificationId) {
            NotificationResponse response = notificationService.getDetail(notificationId);
            return new ResponseAPI<>("Get notification successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Count unread notifications", description = "Count number of unread notifications of a user")
    @GetMapping("/user/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<Long> countUnread() {
            Long count = notificationService.countUnread();
            return new ResponseAPI<>("Count unread notifications successfully", HttpStatus.OK, count);
    }

    @Operation(summary = "Create notification", description = "Create a new notification")
    @PostMapping
    public ResponseAPI<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
            NotificationResponse response = notificationService.createNotification(request);
            return new ResponseAPI<>("Create notification successfully", HttpStatus.CREATED, response);
    }

    @Operation(summary = "Mark a notification as read", description = "Mark a single notification as read")
    @PatchMapping("/{notificationId}/mark-read")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<Boolean> markOneAsRead(@PathVariable String notificationId) {
            Boolean updated = notificationService.markOneIsRead(notificationId);
            return new ResponseAPI<>("Mark notification as read successfully", HttpStatus.OK, updated);
    }

    @Operation(summary = "Mark all notifications as read", description = "Mark all unread notifications of a user as read")
    @PatchMapping("/user/mark-all-read")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<Boolean> markAllAsRead() {
            Boolean updated = notificationService.markAllIsRead();
            return new ResponseAPI<>("Mark all notifications as read successfully", HttpStatus.OK, updated);
    }

    @Operation(summary = "Delete a notification", description = "Delete a single notification by ID")
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<Boolean> deleteNotification(@PathVariable String notificationId) {
            Boolean deleted = notificationService.deleteNotification(notificationId);
            return new ResponseAPI<>("Delete notification successfully", HttpStatus.OK, deleted);
    }

    @Operation(summary = "Delete all notifications of a user", description = "Delete all notifications belonging to a user")
    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseAPI<Boolean> deleteAllByUser() {
            Boolean deleted = notificationService.deleteAllByUser();
            return new ResponseAPI<>("Delete all notifications successfully", HttpStatus.OK, deleted);
    }
}
