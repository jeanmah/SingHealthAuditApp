package com.c2g4.SingHealthWebApp.Notifications;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportBuilder;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;

import javax.mail.MessagingException;

@Component
public class ScheduledDueDateChecker {
    private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
    private static final String EMAIL_SUBJECT = "Overdue Rectifications";

    @Autowired
    OpenAuditRepo openAuditRepo;
    @Autowired
    CompletedAuditRepo completedAuditRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    EmailServiceImpl emailService;

    //runs at 6am everyday
    @Scheduled(cron = "10 55 02 * * ?")
    public void checkDueDates(){
        logger.info("AUTOMATED CHECK DUE DATE START");
        List<Integer> openAuditModelIds = openAuditRepo.getAllOpenAuditsIds();
        //user id, entries
        HashMap<Integer, ArrayList<OverDueAuditEntries>> usersToNotify = new HashMap<>();
        for(int openAuditId: openAuditModelIds){
            logger.info("Openaudit id {}",openAuditId);
            ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo,
                    completedAuditRepo, openAuditId);
            List<ReportEntry> overDueEntries = builder.getOverDueEntries();
            if(overDueEntries.size()==0){
                logger.info("nothing is overdue");
                continue;
            }
            addAllUsersToNotify(usersToNotify,builder,overDueEntries);
        }
        emailUsers(usersToNotify);
    }

    private void addAllUsersToNotify(HashMap<Integer,ArrayList<OverDueAuditEntries>> usersToNotify,
                                    ReportBuilder builder, List<ReportEntry> overDueEntries){
        int managerId = builder.getManager_id();
        int auditorId = builder.getAuditor_id();
        int tenantId = builder.getTenant_id();
        logger.info("{} {} {}", managerId,auditorId,tenantId);

        OverDueAuditEntries overDueAuditEntries = new OverDueAuditEntries(overDueEntries,builder.getOpen_date(),
                builder.getOverall_score(), builder.getReport_id(), managerId, auditorId, tenantId,accountRepo);
        logger.info(overDueAuditEntries.toString());

        addUserToNotify(usersToNotify, overDueAuditEntries,managerId);
        addUserToNotify(usersToNotify, overDueAuditEntries,auditorId);
        addUserToNotify(usersToNotify, overDueAuditEntries,tenantId);
    }

    private void addUserToNotify(HashMap<Integer,ArrayList<OverDueAuditEntries>> usersToNotify,
                                 OverDueAuditEntries overDueAuditEntries, int user_id ){

        if(usersToNotify.containsKey(user_id)){
            usersToNotify.get(user_id).add(overDueAuditEntries);
            logger.info("appending to userstonotify for user {}",user_id);

        } else{
            ArrayList<OverDueAuditEntries> arrayList = new ArrayList<>();
            arrayList.add(overDueAuditEntries);
            usersToNotify.put(user_id, arrayList);
            logger.info("adding to userstonotify for user {}",user_id);
        }
    }


    //email them
    public void emailUsers(HashMap<Integer, ArrayList<OverDueAuditEntries>> usersToNotify) {
        logger.info("num users to notify {}", usersToNotify.size());
        for (int userId : usersToNotify.keySet()) {
            AccountModel accountModel = accountRepo.findByAccId(userId);
            if (accountModel == null) {
                logger.warn("USER WITH ID {} NOT FOUND", userId);
                continue;
            }
            String email = accountModel.getEmail();
            ArrayList<OverDueAuditEntries> overDueAuditEntriesList = usersToNotify.get(userId);
            //OverDueRectificationEmailTemplate emailTemplate = new OverDueRectificationEmailTemplate(usersToNotify.get(userId), accountModel.getRole_id());
            //String emailBody = emailTemplate.getBody();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("overdueEntries", overDueAuditEntriesList);

//            for(OverDueAuditEntires overDueAuditEntires: overDueAuditEntriesList) {
//                AccountModel tenant = overDueAuditEntires.getTenant();
//                AccountModel auditor = overDueAuditEntires.getAuditor();

//                templateModel.put("tenantName", String.format("%s %s",tenant.getFirst_name(), tenant.getLast_name()));
//                templateModel.put("tenantBranch",tenant.getBranch_id());
//                templateModel.put("tenantId",tenant.getAccount_id());
//
//                templateModel.put("auditorName", String.format("%s %s", auditor.getFirst_name(), auditor.getLast_name()));
//                templateModel.put("auditorId",auditor.getAccount_id());
//
//                templateModel.put("auditDate",overDueAuditEntires.getAuditDate());
//                templateModel.put("EarliestRectificationDueDate",overDueAuditEntires.getEarliestOverDue());
//                templateModel.put("NumberOverdue",overDueAuditEntires.overDueEntries.size());

            try {
                emailService.sendMessageUsingThymeleafTemplate(email, EMAIL_SUBJECT, templateModel);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        logger.info("emailed to {}", userId);
    }
    }
}
