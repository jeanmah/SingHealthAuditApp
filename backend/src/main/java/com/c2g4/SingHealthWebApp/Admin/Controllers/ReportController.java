package com.c2g4.SingHealthWebApp.Admin.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListModel;
import com.c2g4.SingHealthWebApp.Admin.Report.Report;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportBuilder;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListNFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@GetMapping("/report/getallquestions")
	public ResponseEntity<List<AuditCheckListModel>> getAllQuestions(@RequestParam(required=true) String type){
		List<AuditCheckListModel> questions = null;
		
		if (type.matches(ResourceString.FB_KEY.toLowerCase())) {
			questions = new ArrayList<AuditCheckListModel>(auditCheckListFBRepo.getAllQuestions());
		}else if(type.matches(ResourceString.NFB_KEY.toLowerCase())) {
			questions = new ArrayList<AuditCheckListModel>(auditCheckListNFBRepo.getAllQuestions());
		}else if(type.matches(ResourceString.SMA_KEY.toLowerCase())) {
			//To-do when the third checklist repo is implemented
		}
		
		if(questions == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(questions);
	}
	
	@GetMapping("/report/getQuestion")
	public ResponseEntity<AuditCheckListModel> getQuestion(
			@RequestParam(required=true) String type, 
			@RequestParam(required=true) int qn_id){
		
		AuditCheckListModel question = null;
		
		if (type.matches(ResourceString.FB_KEY.toLowerCase())) {
			question = auditCheckListFBRepo.getQuestion(qn_id);
		}else if(type.matches(ResourceString.NFB_KEY.toLowerCase())) {
			question = auditCheckListNFBRepo.getQuestion(qn_id);
		}else if(type.matches(ResourceString.SMA_KEY.toLowerCase())) {
			//To-do when the third checklist repo is implemented
		}
		
		if(question == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(question);
	}
	
	@GetMapping("/report/getReport")
	public ResponseEntity<?> getReport(@RequestParam(required=true) int report_id){
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
		
		List<ReportEntry> entries = report.getEntries();
		
		for(ReportEntry entry:entries) {
			List<String> img = entry.getImages();
		}
		
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			String reportJSON = objectmapper.writeValueAsString(report);
			
			
			
			return null;
		} catch (JsonProcessingException e) {
			logger.error("MALFORMED REPORT!");
			return ResponseEntity.unprocessableEntity().build();
		}
	}
	
	
	
	
}
