package com.c2g4.SingHealthWebApp.Notifications;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class NotificationsController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private NotificationsRepo notificationsRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * gets all the current and past notifications for a particular role (Tenant, Auditor, Manager),
     * does not include notifications that are created but not yet released,
     * if no role_id given, will get for the caller user
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param role_id optional String, either Tenant, Auditor or Manager
     * @return JsonArray of all available notifications with keys {"notification_id", "title", "message","create_date",
     * "receipt_date","end_date","to_role_ids","forAuditor","forManager","forTenant"}
     * returns HTTP UNAUTHORIZED if a non-manager asks for a role_id notification that is not their own
     * returns HTTP bad request if no notifications are found
     */
    @GetMapping("/notifications/getAllAvailableNotifications")
    public ResponseEntity<?> getAllAvailableNotifications(@AuthenticationPrincipal UserDetails callerUser,
                                         @RequestParam(required = false, defaultValue = "-1") String role_id){
        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
        }
        role_id = role_id.equals("-1")?callerAccount.getRole_id() : role_id;

        if(!callerAccount.getRole_id().equals(ResourceString.MANAGER_ROLE_KEY)
                && !role_id.equals(callerAccount.getRole_id())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        List<NotificationsModel> unCheckedAllAvailableNotifications = notificationsRepo.getAllAvailableNotifications();
        if(unCheckedAllAvailableNotifications == null) return ResponseEntity.badRequest().body("no notifications found");

        List<NotificationsModel> notificationsModels = addRelevantNotifications(unCheckedAllAvailableNotifications,role_id);

        return ResponseEntity.ok(notificationsModels);
    }

    /**
     * gets all the current notifications for a particular role (Tenant, Auditor, Manager),
     * does not include notifications that are created but not yet released,
     * if no role_id given, will get for the caller user
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param role_id optional String, either Tenant, Auditor or Manager
     * @return JsonArray of current notifications with keys {"notification_id", "title", "message","create_date",
     * "receipt_date","end_date","to_role_ids","forAuditor","forManager","forTenant"}
     * returns HTTP UNAUTHORIZED if a non-manager asks for a role_id notification that is not their own
     * returns HTTP bad request if no notifications are found
     */
    @GetMapping("/notifications/getCurrentNotifications")
    public ResponseEntity<?> getCurrentNotifications(@AuthenticationPrincipal UserDetails callerUser,
                                                     @RequestParam(required = false, defaultValue = "-1") String role_id){
        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
        }
        role_id = role_id.equals("-1")?callerAccount.getRole_id() : role_id;

        if(!callerAccount.getRole_id().equals(ResourceString.MANAGER_ROLE_KEY)
                && !role_id.equals(callerAccount.getRole_id())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        List<NotificationsModel> unCheckedAllCurrentNotifications = notificationsRepo.getCurrentNotifications();
        if(unCheckedAllCurrentNotifications == null) return ResponseEntity.badRequest().body("no notifications found");

        List<NotificationsModel> notificationsModels = addRelevantNotifications(unCheckedAllCurrentNotifications,role_id);

        return ResponseEntity.ok(notificationsModels);
    }

    /**
     * creates a new List of NotificationModels from unCheckedNotifications that are relevant to the role_id
     * @param unCheckedNotifications List of NotificationModel to be checked
     * @param role_id String, either Tenant, Auditor or Manager
     * @return a new List of NotificationModels that are relevant to the role_id
     */
    private List<NotificationsModel> addRelevantNotifications(List<NotificationsModel> unCheckedNotifications, String role_id){
        List<NotificationsModel> notificationsModels = new ArrayList<>();

        for(NotificationsModel notificationsModel: unCheckedNotifications) {
            switch (role_id) {
                case ResourceString.TENANT_ROLE_KEY:
                    if(notificationsModel.isForTenant()) notificationsModels.add(notificationsModel);
                    break;
                case ResourceString.AUDITOR_ROLE_KEY:
                    if(notificationsModel.isForAuditor()) notificationsModels.add(notificationsModel);
                    break;
                case ResourceString.MANAGER_ROLE_KEY:
                    if(notificationsModel.isForManager()) notificationsModels.add(notificationsModel);
                    break;
            }
        }

        return notificationsModels;
    }

    /**
     * gets the notification specified,
     * will only be authorised if the notification is meant for the user or if the user is a manager
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param notification_id int the id of the notification to get
     * @return Current notifications with keys {"notification_id", "title", "message","create_date",
     * "receipt_date","end_date","to_role_ids","forAuditor","forManager","forTenant"}
     * returns HTTP UNAUTHORIZED if a non-manager asks for a notification that is not their own
     * returns HTTP bad request if no notifications are found
     */
    @GetMapping("/notifications/getNotificationByNotificationId")
    public ResponseEntity<?> getNotificationByNotificationId(@AuthenticationPrincipal UserDetails callerUser,
                                                     @RequestParam int notification_id){
        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
        }
        NotificationsModel notificationsModel = notificationsRepo.getNotificationByNotificationId(notification_id);
        if(notificationsModel ==null) return ResponseEntity.badRequest().body("notification not found");
        if(!checkNotificationAuthorization(callerAccount.getRole_id(),notificationsModel)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        return ResponseEntity.ok(notificationsModel);
    }

    /**
     * gets all notifications created by a particular manager that are available to the user, all available to any manager
     * if no creator_id called, gets for the current caller if the current caller is a manager
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param creator_id optional int the account_id of the creator (a manager)
     * @return JsonArray of all notifications created by creator with keys {"notification_id", "title",
     * "message","create_date", "receipt_date","end_date","to_role_ids","forAuditor","forManager","forTenant"}
     * returns HTTP UNAUTHORIZED if a non-manager asks for a role_id notification that is not their own
     * returns HTTP bad request if no notifications are found
     * returns HTTP bad request if creator_id is not set and the caller is not a manager
     */
    @GetMapping("/notifications/getNotificationsByCreatorId")
    public ResponseEntity<?> getNotificationsByCreatorId(@AuthenticationPrincipal UserDetails callerUser,
                                                     @RequestParam(required = false, defaultValue = "-1") int creator_id){
        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
        }
        if(creator_id == -1){
            if(callerAccount.getRole_id().equals(ResourceString.MANAGER_ROLE_KEY)){
                creator_id = callerAccount.getAccount_id();
            } else {
                return ResponseEntity.badRequest().body("creator_id not specified and user is not a manager");
            }
        }
        List<NotificationsModel> uncheckedNotificationsModels = notificationsRepo.getNotificationsByCreatorId(creator_id);
        List<NotificationsModel> notificationModels = new ArrayList<>();
        for(NotificationsModel notificationsModel: uncheckedNotificationsModels){
            if(checkNotificationAuthorization(callerAccount.getRole_id(),notificationsModel)){
                notificationModels.add(notificationsModel);
            }
        }
        return ResponseEntity.ok(notificationModels);
    }

    /**
     * only authorised for managers to create a notification
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param title String the title of the notification
     * @param message String the message body of the notification
     * @param receipt_date Date "YYYY-MM-DD" of when the recipients should start to get the notification
     * @param end_date Date "YYYY-MM-DD" of when the recipients should stop getting the notification
     * @param to_role_ids an int to specify which roles should get the notification,
     *                    it is thought of in binary where MSB is tenant, LSB is manager,
     *                    TAM 1 means for, 0 means not for eg 101 means for tenant, not for auditor, for manager
     * @return HTTP ok with body "notification created with ID: {NEW NOTIFICATION ID}"
     * returns HTTP UNAUTHORIZED if the user is not a manager
     */
    //    only authorised for managers to create a notification
    @PostMapping("/notifications/postNewNotification")
    public ResponseEntity<?> postNewNotification(@AuthenticationPrincipal UserDetails callerUser,
                                                 @RequestPart(value = "title") String title,
                                                 @RequestPart(value = "message") String message,
                                                 @RequestPart(value = "receipt_date") Date receipt_date,
                                                 @RequestPart(value = "end_date") Date end_date,
                                                 @RequestPart(value = "to_role_ids") int to_role_ids){

        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) return ResponseEntity.badRequest().body("user account not found");
        if(!callerAccount.getRole_id().equals(ResourceString.MANAGER_ROLE_KEY)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        Date create_date = new Date(Calendar.getInstance().getTime().getTime());
        NotificationsModel newNotification =
                new NotificationsModel(0,callerAccount.getAccount_id(),title,message,create_date,receipt_date,end_date,to_role_ids);
        newNotification = notificationsRepo.save(newNotification);
        return ResponseEntity.ok("notification created with ID: " + newNotification.getNotification_id());
    }

    /**
     * only authorised for the creator of the notification, requires notification id
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param notification_id an int the id of the notification to modify
     * @param title String the title of the notification
     * @param message String the message body of the notification
     * @param receipt_date Date "YYYY-MM-DD" of when the recipients should start to get the notification
     * @param end_date Date "YYYY-MM-DD" of when the recipients should stop getting the notification
     * @param to_role_ids an int to specify which roles should get the notification,
     *                    it is thought of in binary where MSB is tenant, LSB is manager,
     *                    TAM 1 means for, 0 means not for eg 101 means for tenant, not for auditor, for manager
     * @return HTTP ok with body "notification updated"
     * returns HTTP UNAUTHORIZED if the notification is not created by the user
     */
    //    only authorised for the creator of the notification, requires notification id
    @PostMapping("/notifications/postModifyNotification")
    public ResponseEntity<?> postModifyNotification(@AuthenticationPrincipal UserDetails callerUser,
                                                 @RequestPart(value = "notification_id") int notification_id,
                                                 @RequestPart(value = "title") String title,
                                                 @RequestPart(value = "message") String message,
                                                 @RequestPart(value = "receipt_date") Date receipt_date,
                                                 @RequestPart(value = "end_date") Date end_date,
                                                 @RequestPart(value = "to_role_ids") int to_role_ids){

        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) return ResponseEntity.badRequest().body("user account not found");

        NotificationsModel notificationToModify = notificationsRepo.getNotificationByNotificationId(notification_id);
        if(notificationToModify==null) return ResponseEntity.badRequest().body("notification to modify not found");
        if(!(notificationToModify.getCreator_id()==callerAccount.getAccount_id())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        notificationToModify.setTitle(title);
        notificationToModify.setMessage(message);
        notificationToModify.setReceipt_date(receipt_date);
        notificationToModify.setEnd_date(end_date);
        notificationToModify.setTo_role_ids(to_role_ids);

        notificationsRepo.modifyNotificationById(notification_id,title,message,receipt_date,end_date,to_role_ids);
        return ResponseEntity.ok("notification updated");
    }

    /**
     * only authorised for the creator of the notification, cannot be undone
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param notification_id an int the id of the notification to delete
     * @return HTTP ok with body "notification deleted, cannot be undone"
     * returns HTTP UNAUTHORIZED if the notification is not created by the user
     */
    //    only authorised for the creator of the notification, cannot be undone
    @DeleteMapping("/notifications/deleteNotification")
    public ResponseEntity<?> deleteNotification(@AuthenticationPrincipal UserDetails callerUser,
                                                @RequestParam int notification_id){
        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) return ResponseEntity.badRequest().body("user account not found");

        NotificationsModel notificationToDelete = notificationsRepo.getNotificationByNotificationId(notification_id);
        if(notificationToDelete==null) return ResponseEntity.badRequest().body("notification to modify not found");
        if(!(notificationToDelete.getCreator_id()==callerAccount.getAccount_id())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        notificationsRepo.delete(notificationToDelete);
        if(notificationsRepo.existsById(notificationToDelete.getNotification_id())){
            return ResponseEntity.badRequest().body("notification could not be deleted");
        }
        return ResponseEntity.ok("notification deleted, cannot be undone");
    }

    /**
     * checks if the notification is authorised to be accessed by the role
     * based on if the notification is for them and also if the notification is released for viewing yet
     * @param role_id String, either Tenant, Auditor or Manager
     * @param notificationsModel a NotificationModel to check for authorisation
     * @return a boolean true if authorised, false otherwise
     */
    private boolean checkNotificationAuthorization(String role_id, NotificationsModel notificationsModel){
        if(role_id.equals(ResourceString.MANAGER_ROLE_KEY)) return true;
        Calendar calendar = Calendar.getInstance();
        if(calendar.getTime().before(notificationsModel.getReceipt_date())) return false;
        if(role_id.equals(ResourceString.AUDITOR_ROLE_KEY)){
            return notificationsModel.isForAuditor();
        } else if(role_id.equals(ResourceString.TENANT_ROLE_KEY)){
            return notificationsModel.isForTenant();
        } else {
            return false;
        }
    }

    /**
     * converts a UserDetails object into an AccountModel object
     * @param callerUser a UserDetail object
     * @return a corresponding AccountModel object
     */
    private AccountModel convertUserDetailsToAccount(UserDetails callerUser){
        logger.info("CALLER USER USERNAME {}",callerUser.getUsername());
        return accountRepo.findByUsername(callerUser.getUsername());
    }

}