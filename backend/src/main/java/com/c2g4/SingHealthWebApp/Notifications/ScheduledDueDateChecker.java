package com.c2g4.SingHealthWebApp.Notifications;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportBuilder;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @Scheduled(cron = "0 25 15 * * ?")
    public void checkDueDates(){
        logger.info("AUTOMATED CHECK DUE DATE START");
        List<Integer> openAuditModelIds = openAuditRepo.getAllOpenAuditsIds();
        //user id, entries
        HashMap<Integer, ArrayList<OverDueAuditEntires>> usersToNotify = new HashMap<>();
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

    private void addAllUsersToNotify(HashMap<Integer,ArrayList<OverDueAuditEntires>> usersToNotify,
                                    ReportBuilder builder, List<ReportEntry> overDueEntries){
        int managerId = builder.getManager_id();
        int auditorId = builder.getAuditor_id();
        int tenantId = builder.getTenant_id();
        logger.info("{} {} {}", managerId,auditorId,tenantId);

        OverDueAuditEntires overDueAuditEntires = new OverDueAuditEntires(overDueEntries,builder.getReport_id(), managerId, auditorId, tenantId);
        logger.info(overDueAuditEntires.toString());

        addUserToNotify(usersToNotify,overDueAuditEntires,managerId);
        addUserToNotify(usersToNotify,overDueAuditEntires,auditorId);
        addUserToNotify(usersToNotify,overDueAuditEntires,tenantId);
    }

    private void addUserToNotify(HashMap<Integer,ArrayList<OverDueAuditEntires>> usersToNotify,
                                 OverDueAuditEntires overDueAuditEntires, int user_id ){

        if(usersToNotify.containsKey(user_id)){
            usersToNotify.get(user_id).add(overDueAuditEntires);
            logger.info("appending to userstonotify for user {}",user_id);

        } else{
            ArrayList<OverDueAuditEntires> arrayList = new ArrayList<>();
            arrayList.add(overDueAuditEntires);
            usersToNotify.put(user_id, arrayList);
            logger.info("adding to userstonotify for user {}",user_id);
        }
    }


    //email them
    public void emailUsers(HashMap<Integer, ArrayList<OverDueAuditEntires>> usersToNotify){
        logger.info("num users to notify {}",usersToNotify.size());
        for(int userId:usersToNotify.keySet()){
            AccountModel accountModel = accountRepo.findByAccId(userId);
            if(accountModel ==null){
                logger.warn("USER WITH ID {} NOT FOUND",userId);
                continue;
            }
            String email = accountModel.getEmail();
            OverDueRectificationEmailTemplate emailTemplate = new OverDueRectificationEmailTemplate(usersToNotify.get(userId), accountModel.getRole_id());
            String emailBody = emailTemplate.getBody();
            emailService.sendSimpleMessage(email,EMAIL_SUBJECT,emailBody);
            logger.info("emailed to {}",userId);
        }
    }


    //notifications
    public static class OverDueAuditEntires{
        private final List<ReportEntry> overDueEntries;
        private final int reportId;
        private final int managerId;
        private final int auditorId;
        private final int tenantId;

        public OverDueAuditEntires(List<ReportEntry> overDueEntries, int reportId, int managerId, int auditorId, int tenantId) {
            this.overDueEntries = overDueEntries;
            this.reportId = reportId;
            this.managerId = managerId;
            this.auditorId = auditorId;
            this.tenantId = tenantId;
        }

        public List<ReportEntry> getOverDueEntries() {
            return overDueEntries;
        }

        public int getReportId() {
            return reportId;
        }

        public int getManagerId() {
            return managerId;
        }

        public int getAuditorId() {
            return auditorId;
        }

        public int getTenantId() {
            return tenantId;
        }

        public int numEntries(){
            return overDueEntries.size();
        }

        @Override
        public String toString() {
            return "OverDueAuditEntires{" +
                    "num overDueEntries=" + overDueEntries.size() +
                    ", reportId=" + reportId +
                    ", managerId=" + managerId +
                    ", auditorId=" + auditorId +
                    ", tenantId=" + tenantId +
                    '}';
        }
    }
}