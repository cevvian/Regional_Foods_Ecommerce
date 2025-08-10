package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, String> {
    @Query("""
    SELECT n FROM Notification n
    WHERE n.user.userId = :userId
      AND (:isRead IS NULL OR n.isRead = :isRead)
    """)
    Page<Notification> findByUserIdAndIsReadOptional(
            Pageable pageable,
            @Param("userId") String userId,
            @Param("isRead") Boolean isRead
    );

    @Query("""
    SELECT n FROM Notification n
    WHERE n.user.userId = :userId
      AND (n.isRead = FALSE)
    """)
    List<Notification> findByUserIdAndIsReadNot(
            @Param("userId") String userId
    );

    @Query("""
    SELECT n FROM Notification n
    WHERE n.user.userId = :userId
    """)
    List<Notification> findByUserId(
            @Param("userId") String userId
    );

    @Query("""
    SELECT COUNT(n) FROM Notification n
    WHERE n.user.userId = :userId
      AND (n.isRead = FALSE)
    """)
    Long countByUserIdAndIsReadNot(@Param("userId") String userId);

//    @Query("""
//    SELECT COUNT(n) FROM Notification n
//    WHERE n.user.userId = :userId
//      AND (:isRead IS NULL OR n.isRead = FALSE)
//    """)
//    Long countByUserIdAndIsReadNot(@Param("userId") String userId);

    @Query("""
    SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END
    FROM Notification n
    WHERE n.title = :title AND n.content = :content
""")
    Boolean existsNotificationByContentAndTitle(@Param("title") String title, @Param("content") String content);
}
