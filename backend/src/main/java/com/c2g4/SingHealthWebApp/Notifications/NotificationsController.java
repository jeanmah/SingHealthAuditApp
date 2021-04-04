package com.c2g4.SingHealthWebApp.Notifications;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

        List<NotificationsModel> notificationsModels = new ArrayList<>();
        for(NotificationsModel notificationsModel: unCheckedAllAvailableNotifications) {
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
                default:
                    return ResponseEntity.badRequest().body("role_id cannot be found");
            }
        }
        return ResponseEntity.ok(notificationsModels);
    }

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

        List<NotificationsModel> notificationsModels = new ArrayList<>();
        for(NotificationsModel notificationsModel: unCheckedAllCurrentNotifications) {
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
                default:
                    return ResponseEntity.badRequest().body("role_id cannot be found");
            }
        }
        return ResponseEntity.ok(notificationsModels);
    }


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

    @GetMapping("/notifications/getNotificationsByCreatorId")
    public ResponseEntity<?> getNotificationsByCreatorId(@AuthenticationPrincipal UserDetails callerUser,
                                                     @RequestParam(required = false, defaultValue = "-1") int creator_id){
        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount==null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
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

    private AccountModel convertUserDetailsToAccount(UserDetails callerUser){
        logger.info("CALLER USER USERNAME {}",callerUser.getUsername());
        return accountRepo.findByUsername(callerUser.getUsername());
    }

}