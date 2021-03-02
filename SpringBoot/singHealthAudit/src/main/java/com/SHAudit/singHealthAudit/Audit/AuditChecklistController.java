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
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
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

    //expected for filledCheckList
    // "key:[{ \"qn_id\" : \"12321\", \"passFail\" : \"true\" , \"remarks\" : \"null\"},
    //      { \"qn_id\" : \"12322\", \"passFail\" : \"true\" , \"remarks\" : \"null\"}]";
    @PostMapping("/a/{tenantId}/fbchecklist/submission")
    public ResponseEntity<?> submitFBChecklist(
            @RequestParam("files") MultipartFile[] images,
            @RequestPart(value = "filledChecklist", required = true) String filledChecklist,
            @AuthenticationPrincipal UserDetails auditorUser, @PathVariable("tenantId") int tenantId)  {
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
            logger.warn("JSON PROCESSING EXCEPTION FBCHECKLIST POST");
            return ResponseEntity.badRequest().body(null);
        }

        int imageCounter = 0;
        //TODO: figure out if this conversion is gonna screw things up
        for(Entry auditorEntry: auditorEntryList){
            AuditorEntry auditorEntryAE = (AuditorEntry) auditorEntry;
            if(!auditorEntryAE.getStatus()){
                MultipartFile uploadedImage = images[imageCounter];
                if(uploadedImage == null) {
                    logger.warn("UPLOADED IMAGE NUM {} NULL FBCHECKLIST POST",imageCounter);
                    return ResponseEntity.badRequest().body(null);
                }
                BufferedImage img;
                try {
                    img = ImageIO.read(new File(uploadedImage.getOriginalFilename()));
                    auditorEntry.setEvidence(img);
                    imageCounter++;

                } catch (IOException e) {
                    logger.warn("UPLOADED IMAGE NUM {} CANNOT OPEN FILE FBCHECKLIST POST",imageCounter);
                    return ResponseEntity.badRequest().body(null);
                }
            }
        }
        //TODO: implement method to calculate score
        int overall_score = 0;

        java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
        Date sqlCurrentDate = new Date(utilCurrentDate.getTime());

        int manager_id = auditorRepository.getManagerIDfromAuditorID(auditorId);

        if(imageCounter>0){
            Open_Report open_report = new Open_Report(0, overall_score, false, sqlCurrentDate,
                    auditorEntryList, sqlCurrentDate);
            try {
                OpenAudits openAudits = new OpenAudits( tenantId, auditorId, manager_id, sqlCurrentDate,
                        sqlCurrentDate, "what to put here", overall_score, objectMapper.writeValueAsString(open_report), true, true);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.warn("OpenAudits saving to mysql fail FBCHECKLIST POST");
                return ResponseEntity.badRequest().body(null);
            }

        } else {
            Close_Report close_report = new Close_Report( 0, overall_score, true,
                    sqlCurrentDate, auditorEntryList, sqlCurrentDate);
            try {
                CompletedAudits completedAudits = new CompletedAudits(tenantId, auditorId, manager_id, sqlCurrentDate, sqlCurrentDate,
                    "Overallremarks", overall_score,  objectMapper.writeValueAsString(close_report));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.warn("completedAudits saving to mysql fail FBCHECKLIST POST");
                return ResponseEntity.badRequest().body(null);
            }
        }
        return ResponseEntity.ok(overall_score);
    }

    //TODO: implement
    //expected for filledCheckList
    // "[{ \"qn_id\" : \"12321\", \"passFail\" : \"true\" , \"remarks\" : \"null\"},
    //      { \"qn_id\" : \"12322\", \"passFail\" : \"true\" , \"remarks\" : \"null\"}]";
    @PostMapping("/a/{tenantId}/nfbchecklist/submission")
    public ResponseEntity<?> submitNFBChecklist(
            @RequestParam("files") MultipartFile[] images,
            @RequestPart(value = "filledChecklist", required = true) String filledChecklist,
            @AuthenticationPrincipal Account auditorAccount, @PathVariable("tenantId") int tenantId)  {
        return ResponseEntity.ok(0);
    }

    //TODO: implement
    @GetMapping("/a/tenantid/score")
    public ResponseEntity<?> getLastAuditScore(@PathVariable("tenantId") int tenantId){
        return ResponseEntity.ok(0);
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
