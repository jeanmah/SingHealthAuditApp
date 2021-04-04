package com.c2g4.SingHealthWebApp.Notifications;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface NotificationsRepo extends CrudRepository<NotificationsModel, Integer> {
    @Query("SELECT * FROM Notifications WHERE notification_id = :notification_id LIMIT 1")
    NotificationsModel getNotificationByNotificationId(@Param("notification_id") int notification_id);

    @Query("SELECT * FROM Notifications WHERE creator_id = :creator_id")
    List<NotificationsModel> getNotificationsByCreatorId(@Param("creator_id") int creator_id);

    @Query("SELECT * FROM Notifications WHERE to_role_ids&4 = 4 AND receipt_date <= curdate()")
    List<NotificationsModel> getAllAvailableNotificationsForTenants();

    @Query("SELECT * FROM Notifications WHERE to_role_ids&2 = 2 AND receipt_date <= curdate()")
    List<NotificationsModel> getAllAvailableNotificationsForAuditor();

    @Query("SELECT * FROM Notifications WHERE to_role_ids&1 = 1 AND receipt_date <= curdate()")
    List<NotificationsModel> getAllAvailableNotificationsForManagers();

    @Query("SELECT * FROM Notifications WHERE to_role_ids&4 = 4 AND receipt_date <= curdate() AND end_date >= curdate()")
    List<NotificationsModel> getCurrentNotificationsForTenants();

    @Query("SELECT * FROM Notifications WHERE to_role_ids&2 = 2 AND receipt_date <= curdate() AND end_date >= curdate()")
    List<NotificationsModel> getCurrentNotificationsForAuditors();

    @Query("SELECT * FROM Notifications WHERE to_role_ids&1 = 1 AND receipt_date <= curdate() AND end_date >= curdate()")
    List<NotificationsModel> getCurrentNotificationsForManagers();

    @Modifying
    @Query("UPDATE Notifications n SET n.title = :title, n.message = :message, " +
            "n.receipt_date = :receipt_date, n.end_date = :end_date, n.to_role_ids = :to_role_ids " +
            "WHERE n.notification_id = :notification_id")
    void modifyNotificationById(@Param("notification_id") int notification_id, @Param("title") String title,
                             @Param("message") String message, @Param("receipt_date") Date receipt_date,
                             @Param("end_date") Date end_date, @Param("to_role_ids") int to_role_ids);
}
