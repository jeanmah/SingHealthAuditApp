package com.SHAudit.singHealthAudit.Audit;

import com.SHAudit.singHealthAudit.Admin.Auditor.AuditorRepository;
import com.SHAudit.singHealthAudit.Admin.Manager.ManagerRepository;
import com.SHAudit.singHealthAudit.Admin.Tenant.TenantRepository;
import com.SHAudit.singHealthAudit.Admin.mySQLAccount.Account;
import com.SHAudit.singHealthAudit.Admin.mySQLAccount.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class AuditChecklistController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditCheckListFBRepository auditCheckListFBRepository;
    @Autowired
    private AuditCheckListNFBRepository auditCheckListNFBRepository;
    @Autowired
    private CompletedAuditsRepository completedAuditsRepository;
    @Autowired
    private OpenAuditsRepository openAuditsRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private AuditorRepository auditorRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/a/auditchecklist/{tenantId}/fbchecklist/{category}")
    public ResponseEntity<?> getFBCheckListCategoryQuestions(@PathVariable("category") String category) {
        List<AuditCheckListFB> questions = auditCheckListFBRepository.getQuestionByCategory(category);
        if (questions == null || questions.size() == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/a/auditchecklist/{tenantId}/nfbchecklist/{category}")
    public ResponseEntity<?> getNFBCheckListCategoryQuestions(@PathVariable("category") String category) {
        List<AuditCheckListNFB> questions = auditCheckListNFBRepository.getQuestionByCategory(category);
        if (questions == null || questions.size() == 0){
            logger.warn("no such category {}",category);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        logger.info(questions.get(0).getRequirement());
        return ResponseEntity.ok(questions);
    }


    private String checkNonCompliances(List<Entry> auditorEntryList, MultipartFile[] images, String category){
        //cat, number of questions in cat, weight of cat, num wrong in cat
        HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap = new HashMap<>();
        int imageCounter = 0;
        //TODO: figure out if this conversion is gonna screw things up
        for(Entry auditorEntry: auditorEntryList){
            AuditorEntry auditorEntryAE = (AuditorEntry) auditorEntry;

            String qnCategory = category.equals("fbchecklist") ?
                    auditCheckListFBRepository.getCategoryByQnID(auditorEntry.getQn_id())
                    : auditCheckListNFBRepository.getCategoryByQnID(auditorEntry.getQn_id());

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
                            auditCheckListFBRepository.getWeightByQnID(auditorEntry.getQn_id())
                            : auditCheckListNFBRepository.getWeightByQnID(auditorEntry.getQn_id());
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,weight,1,1);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
            } else{
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertCorrect();
                } else{
                    double weight = category.equals("fbchecklist") ?
                            auditCheckListFBRepository.getWeightByQnID(auditorEntry.getQn_id())
                            : auditCheckListNFBRepository.getWeightByQnID(auditorEntry.getQn_id());
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
            @AuthenticationPrincipal UserDetails auditorUser, @PathVariable("tenantId") int tenantId,
            @PathVariable("checklistType") String checklistType)  {
        Account auditorAccount = accountRepository.findByUsername(auditorUser.getUsername());
        int auditorId = auditorAccount.getAccount_id();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AuditorEntry.class, new CustomAuditorEntryDeserializer());
        objectMapper.registerModule(module);

        JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class, AuditorEntry.class);
        List<Entry> auditorEntryList;
        try {
            auditorEntryList = objectMapper.readValue(filledChecklist, customClassCollection);
        } catch (JsonProcessingException e) {
            logger.warn("JSON PROCESSING EXCEPTION {} POST",checklistType);
            return ResponseEntity.badRequest().body(null);
        }
//
//        int imageCounter = 0;
//        //TODO: figure out if this conversion is gonna screw things up
//        for(Entry auditorEntry: auditorEntryList){
//            AuditorEntry auditorEntryAE = (AuditorEntry) auditorEntry;
//            if(!auditorEntryAE.getStatus()){
//                MultipartFile uploadedImage = images[imageCounter];
//                if(uploadedImage == null) {
//                    logger.warn("UPLOADED IMAGE NUM {} NULL FBCHECKLIST POST",imageCounter);
//                    return ResponseEntity.badRequest().body(null);
//                }
//                BufferedImage img;
//                try {
//                    logger.warn("UPLOADED IMAGE NAME {} FBCHECKLIST POST",uploadedImage.getOriginalFilename());
//
//                    String base64img = Base64.getEncoder().encodeToString(uploadedImage.getBytes());
//                    auditorEntry.setEvidence(base64img);
//                    imageCounter++;
//
//
//                } catch (IOException e) {
//                    logger.warn("UPLOADED IMAGE NUM {} CANNOT OPEN FILE FBCHECKLIST POST",imageCounter);
//                    return ResponseEntity.badRequest().body(null);
//                }
//            }
//        }
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

        int manager_id = auditorRepository.getManagerIDfromAuditorID(auditorId);

        if(auditScore<100){
            Open_Report open_report = new Open_Report(0, (int)auditScore, false, sqlCurrentDate,
                    auditorEntryList, sqlCurrentDate);
            try {

                logger.info("doing objectmapping report fbchecklist");
                String report = objectMapper.writeValueAsString(open_report);
                logger.info("finish objectmapping report fbchecklist");


                OpenAudits openAudits = new OpenAudits(tenantId, auditorId, manager_id, sqlCurrentDate,
                        sqlCurrentDate, "what to put here", (int)auditScore, report, 1, 1);
                openAuditsRepository.save(openAudits);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.warn("OpenAudits saving to mysql fail FBCHECKLIST POST");
                return ResponseEntity.badRequest().body(null);
            }
            int openReportId = openAuditsRepository.getReportIdFromTenantId(tenantId);
            tenantRepository.updateLatestAuditByTenantId(tenantId,openReportId);

        } else {
            Close_Report close_report = new Close_Report( 0, (int)auditScore, true,
                    sqlCurrentDate, auditorEntryList, sqlCurrentDate);
            try {
                CompletedAudits completedAudits = new CompletedAudits(tenantId, auditorId, manager_id, sqlCurrentDate, sqlCurrentDate,
                    "Overallremarks", (int)auditScore,  objectMapper.writeValueAsString(close_report));
                completedAuditsRepository.save(completedAudits);

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

/*
    /a/auditorid/tenantid/fbchecklist/{n}{category} --> each checklist page for fb
    /a/auditorid/tenantid/nfbchecklist/{n}{category --> each checklist page for nfb
    /a/auditorid/tenantid/safetychecklist/{n}{category}} --> each checklist page for safety dont have
    /a/auditorid/tenantid/fbchecklist/submission
    /a/auditorid/tenantid/nfbchecklist/submission
    /a/auditorid/tenantid/safetychecklist/submission
    /a/auditorid/tenantid/score
*/
