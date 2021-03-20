package com.c2g4.SingHealthWebApp.Admin.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListModel;
import com.c2g4.SingHealthWebApp.Admin.Report.ClosedReport;
import com.c2g4.SingHealthWebApp.Admin.Report.CustomReportEntryDeserializer;
import com.c2g4.SingHealthWebApp.Admin.Report.OpenReport;
import com.c2g4.SingHealthWebApp.Admin.Report.Report;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportBuilder;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListNFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class ReportController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	AuditCheckListFBRepo auditCheckListFBRepo;
	@Autowired
	AuditCheckListNFBRepo auditCheckListNFBRepo;
	@Autowired
	OpenAuditRepo openAuditRepo;
	@Autowired
	CompletedAuditRepo completedAuditRepo;
	@Autowired
	AccountRepo accountRepo;
	@Autowired
	AuditorRepo auditorRepo;
	@Autowired
	TenantRepo tenantRepo;
	@Autowired
	ManagerRepo managerRepo;
	
	@GetMapping("/report/getAllQuestions")
	public ResponseEntity<List<AuditCheckListModel>> getAllQuestions(
			@RequestParam(value="type", required=true) String type){
		logger.info("Questions of checklist type " + type + " requested.");
		List<AuditCheckListModel> questions = null;
		
		if (type.toUpperCase().matches(ResourceString.FB_KEY)) {
			logger.info("Something happened");
			questions = new ArrayList<AuditCheckListModel>(auditCheckListFBRepo.getAllQuestions());
		}else if(type.toUpperCase().matches(ResourceString.NFB_KEY)) {
			questions = new ArrayList<AuditCheckListModel>(auditCheckListNFBRepo.getAllQuestions());
		}else if(type.toUpperCase().matches(ResourceString.SMA_KEY)) {
			//To-do when the third checklist repo is implemented
		}
		if(questions == null) {
			return ResponseEntity.notFound().build();
		}
		logger.info(questions.toString());
		return ResponseEntity.ok(questions);
	}
	
	@GetMapping("/report/getQuestion")
	public ResponseEntity<AuditCheckListModel> getQuestion(
			@RequestParam(value="type", required=true) String type, 
			@RequestParam(value="qn_id", required=true) int qn_id){
		
		AuditCheckListModel question = null;
		
		if (type.toUpperCase().matches(ResourceString.FB_KEY)) {
			question = auditCheckListFBRepo.getQuestion(qn_id);
		}else if(type.toUpperCase().matches(ResourceString.NFB_KEY)) {
			question = auditCheckListNFBRepo.getQuestion(qn_id);
		}else if(type.toUpperCase().matches(ResourceString.SMA_KEY)) {
			//To-do when the third checklist repo is implemented
		}
		
		if(question == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(question);
	}
	
	@PostMapping(value="/report/postReportSubmission")
	public ResponseEntity<?> postReportSubmission(
			@RequestParam(value = "type", required = true) String report_type,
            @RequestPart(value = "checklist", required = true) String checklist,
			@RequestParam(value = "tenant_id", required = true) int tenant_id,
			@RequestParam(value = "remarks", required = true) String remarks,
            @AuthenticationPrincipal UserDetails auditorUser){
		logger.info("Report upload request received.");
		
		AccountModel auditorAccount = accountRepo.findByUsername(auditorUser.getUsername());
		int auditor_id = auditorAccount.getAccount_id();
		int manager_id = auditorRepo.getManagerIDfromAuditorID(auditor_id);
		
        List<ReportEntry> entryList;

		ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ReportEntry.class, new CustomReportEntryDeserializer());
        objectMapper.registerModule(module);
        JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class, ReportEntry.class);
        
        try {
            entryList = objectMapper.readValue(checklist, customClassCollection);
        } catch (JsonProcessingException e) {
            logger.warn("JSON PROCESSING EXCEPTION {} POST",report_type);
            return ResponseEntity.badRequest().body(null);
        }
        
        for(int i = 0; i < entryList.size(); i++) {
        	entryList.get(i).setEntry_id(i);
        }
        
        ReportBuilder builder = ReportBuilder.getNewReportBuilder(openAuditRepo, completedAuditRepo);
        builder.setUserIDs(tenant_id, auditor_id, manager_id).setEntries(entryList);
        
        int auditScore = (int) builder.markEntries(auditCheckListFBRepo, auditCheckListNFBRepo, report_type);
    	if(auditScore == -1) {return ResponseEntity.badRequest().body("Report type does not exist");}
    	
        if(auditScore<100){
        	builder.setOverall_remarks(remarks).setOverall_score(auditScore).setNeed(1, 0, 0);
        	OpenReport report = (OpenReport) builder.build();
        	if(!builder.saveReport(report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}
        } else {
        	builder.setOverall_remarks(remarks).setOverall_score(auditScore).setOverall_statusAsClosed();
        	ClosedReport report = (ClosedReport) builder.build();
        	if(!builder.saveReport(report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}
        }
        logger.info("Report Submission Upload Completed.");
    	return ResponseEntity.ok(auditScore);
	}
	
	@GetMapping("/report/getReport")
	public ResponseEntity<?> getReport(@RequestParam(required=true) int report_id){
		logger.info("Report of id " + report_id + " requested.");
		ReportBuilder builder = ReportBuilder.getNewReportBuilder(openAuditRepo, completedAuditRepo);
		Report report = null;
		if(builder.checkOpenReportExists(report_id)) {
			report = builder.loadOpenReport(report_id);
		}else if (builder.checkClosedReportExists(report_id)) {
			report = builder.loadClosedReport(report_id);
		}
		if(report == null) {
			return ResponseEntity.notFound().build();
		}
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			String reportJSON = objectmapper.writeValueAsString(report);
			return ResponseEntity.ok(report);
		} catch (JsonProcessingException e) {
			logger.error("MALFORMED REPORT!");
			return ResponseEntity.unprocessableEntity().build();
		}
	}
	
	
	
	
}
