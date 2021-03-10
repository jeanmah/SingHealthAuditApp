package com.c2g4.SingHealthWebApp.Admin.Controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListFBModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListNFBModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditModelBuilder;
import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Report.AuditorReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Report.ClosedReport;
import com.c2g4.SingHealthWebApp.Admin.Report.CustomAuditorEntryDeserializer;
import com.c2g4.SingHealthWebApp.Admin.Report.OpenReport;
import com.c2g4.SingHealthWebApp.Admin.Report.Report;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListNFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class AuditCheckListController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditCheckListFBRepo auditCheckListFBRepo;
    @Autowired
    private AuditCheckListNFBRepo auditCheckListNFBRepo;
    @Autowired
    private CompletedAuditRepo completedAuditRepo;
    @Autowired
    private OpenAuditRepo openAuditRepo;
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private AuditorRepo auditorRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private AccountRepo accountRepo;

    @GetMapping("/a/auditchecklist/{tenantId}/fbchecklist/{category}")
    public ResponseEntity<?> getFBCheckListCategoryQuestions(@PathVariable("category") String category) {
        List<AuditCheckListFBModel> questions = auditCheckListFBRepo.getQuestionByCategory(category);
        if (questions == null || questions.size() == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/a/auditchecklist/{tenantId}/nfbchecklist/{category}")
    public ResponseEntity<?> getNFBCheckListCategoryQuestions(@PathVariable("category") String category) {
        List<AuditCheckListNFBModel> questions = auditCheckListNFBRepo.getQuestionByCategory(category);
        if (questions == null || questions.size() == 0){
            logger.warn("no such category {}",category);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        logger.info(questions.get(0).getRequirement());
        return ResponseEntity.ok(questions);
    }


    private String checkNonCompliances(List<ReportEntry> auditorEntryList, MultipartFile[] images, String category){
        //cat, number of questions in cat, weight of cat, num wrong in cat
        HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap = new HashMap<>();
        int imageCounter = 0;
        //TODO: figure out if this conversion is gonna screw things up
        for(ReportEntry auditorEntry: auditorEntryList){
            AuditorReportEntry auditorEntryAE = (AuditorReportEntry) auditorEntry;

            String qnCategory = category.equals("fbchecklist") ?
                    auditCheckListFBRepo.getCategoryByQnID(auditorEntry.getQn_id())
                    : auditCheckListNFBRepo.getCategoryByQnID(auditorEntry.getQn_id());

            if(!auditorEntryAE.getStatus()){
                MultipartFile uploadedImage = images[imageCounter];
                if(uploadedImage == null) {
                    logger.warn("UPLOADED IMAGE NUM {} NULL CHECKLIST POST",imageCounter);
                    return "UPLOADED IMAGE NULL CHECKLIST POST";
                }
                try {
                    logger.warn("UPLOADED IMAGE NAME {} FBCHECKLIST POST",uploadedImage.getOriginalFilename());

                    String base64img = Base64.getEncoder().encodeToString(uploadedImage.getBytes());
                    auditorEntry.setEvidence(base64img);
                    imageCounter++;

                } catch (IOException e) {
                    logger.warn("UPLOADED IMAGE NUM {} CANNOT OPEN FILE CHECKLIST POST",imageCounter);
                    return "UPLOADED IMAGE CANNOT OPEN FILE CHECKLIST POST";
                }
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertWrong();
                } else{
                    double weight = category.equals("fbchecklist") ?
                            auditCheckListFBRepo.getWeightByQnID(auditorEntry.getQn_id())
                            : auditCheckListNFBRepo.getWeightByQnID(auditorEntry.getQn_id());
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,weight,1,1);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
            } else{
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertCorrect();
                } else{
                    double weight = category.equals("fbchecklist") ?
                            auditCheckListFBRepo.getWeightByQnID(auditorEntry.getQn_id())
                            : auditCheckListNFBRepo.getWeightByQnID(auditorEntry.getQn_id());
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,weight,1,0);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
            }
        }
        return String.valueOf(calculateScore(checklistCategoryScoresHashMap));
    }

    private double calculateScore(HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap){
        if(checklistCategoryScoresHashMap.isEmpty()) return 0;
        double totalScore = 0;
        for(ChecklistCategoryScores categoryScores: checklistCategoryScoresHashMap.values()){
            totalScore+= categoryScores.getScore();
        }
        return totalScore;
    }

    //expected for filledCheckList
    // "key:[{ \"qn_id\" : \"12321\", \"passFail\" : true , \"remarks\" : null, \"severity\": 0123},
    //      { \"qn_id\" : \"12322\", \"passFail\" : true , \"remarks\" : null,\"severity\": 0123}]";
    @PostMapping(value = "/a/{tenantId}/{checklistType}/submission", consumes = {"multipart/form-data"})
    public ResponseEntity<?> submitFBChecklist(
            @RequestParam(value = "files", required = true) MultipartFile[] images,
            @RequestPart(value = "filledChecklist", required = true) String filledChecklist,
            @AuthenticationPrincipal UserDetails auditorUser, 
            @PathVariable("tenantId") int tenant_id,
            @PathVariable("checklistType") String checklistType)  {
        AccountModel auditorAccount = accountRepo.findByUsername(auditorUser.getUsername());
        int auditor_id = auditorAccount.getAccount_id();
        
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AuditorReportEntry.class, new CustomAuditorEntryDeserializer());
        objectMapper.registerModule(module);

        JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class, AuditorReportEntry.class);
        List<ReportEntry> auditorEntryList;
        try {
            auditorEntryList = objectMapper.readValue(filledChecklist, customClassCollection);
        } catch (JsonProcessingException e) {
            logger.warn("JSON PROCESSING EXCEPTION {} POST",checklistType);
            return ResponseEntity.badRequest().body(null);
        }
        //TODO: implement method to calculate score
        String retNonCompliances;
        double auditScore;
        retNonCompliances = checkNonCompliances(auditorEntryList, images,checklistType);

        try {
            auditScore = Double.parseDouble(retNonCompliances) *100.0;
        } catch (NumberFormatException e){
            return ResponseEntity.badRequest().body(retNonCompliances);
        }

        java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
        Date sqlCurrentDate = new Date(utilCurrentDate.getTime());

        int manager_id = auditorRepo.getManagerIDfromAuditorID(auditor_id);

        if(auditScore<100){
            Report openreport = new OpenReport((int)auditScore, false, sqlCurrentDate,
                    auditorEntryList, sqlCurrentDate);
            try {
                logger.info("doing objectmapping report fbchecklist");
                String report = objectMapper.writeValueAsString(openreport);
                logger.info("finish objectmapping report fbchecklist");
                
                AuditModelBuilder builder = new AuditModelBuilder();
                builder.setReportId(0).setUserIDs(tenant_id, auditor_id, manager_id)
                .setOverallRemarks("Idk").setOverallScore((int)auditScore).setReportData(report)
                .setNeed(1,1,1);
                
                OpenAuditModel openAudit = (OpenAuditModel) builder.build();
                openAuditRepo.save(openAudit);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.warn("OpenAudits saving to mysql fail FBCHECKLIST POST");
                return ResponseEntity.badRequest().body(null);
            }
            //int openReportId = openAuditRepo.getReportIdFromTenantId(tenantId);
            //tenantRepo.updateLatestAuditByTenantId(tenantId, openReportId);

        } else {
        	//The first date should not be sqlCurrentDat
            ClosedReport closereport = new ClosedReport((int)auditScore, true, sqlCurrentDate, auditorEntryList,
            		sqlCurrentDate);
            try {
            	AuditModelBuilder builder = new AuditModelBuilder();
            	builder.setReportId(0).setUserIDs(tenant_id, auditor_id, manager_id)
            	.setOverallRemarks("Idk").setOverallScore((int)auditScore)
            	.setReportData(objectMapper.writeValueAsString(closereport));
            	
                CompletedAuditModel completedAudits = (CompletedAuditModel) builder.build();
                completedAuditRepo.save(completedAudits);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.warn("completedAudits saving to mysql fail FBCHECKLIST POST");
                return ResponseEntity.badRequest().body(null);
            }

            //TODO: add completed audit to tenant
        }
        return ResponseEntity.ok((int)auditScore);
    }

    //TODO: implement
    @GetMapping("/a/tenantid/score")
    public ResponseEntity<?> getLastAuditScore(@PathVariable("tenantId") int tenantId){
        return ResponseEntity.ok(0);
    }

    private static class ChecklistCategoryScores{

        private final String categoryName;
        private final double weight;
        private double totalNumQn;
        private double numWrong;

        public ChecklistCategoryScores(String categoryName, double weight, int totalNumQn, int numWrong) {
            this.categoryName = categoryName;
            this.weight = weight;
            this.totalNumQn = totalNumQn;
            this.numWrong = numWrong;
        }

        public double getScore(){
            return ((totalNumQn-numWrong)/totalNumQn)*weight;
        }

        public void insertWrong(){
            numWrong++;
            totalNumQn++;
        }

        public void insertCorrect(){
            totalNumQn++;
        }

        public String getCategoryName() {
            return categoryName;
        }
    }

}
