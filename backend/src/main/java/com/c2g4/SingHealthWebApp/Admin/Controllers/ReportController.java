package com.c2g4.SingHealthWebApp.Admin.Controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Report.ClosedReport;
import com.c2g4.SingHealthWebApp.Admin.Report.Component_Status;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;

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
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
        
        ReportBuilder builder = ReportBuilder.getNewReportBuilder(openAuditRepo, completedAuditRepo);
        builder.setUserIDs(tenant_id, auditor_id, manager_id).setEntries(entryList);
        builder.setReportType(report_type);
        
        int auditScore = (int) builder.markReport(auditCheckListFBRepo, auditCheckListNFBRepo);
    	if(auditScore == -1) {return ResponseEntity.badRequest().body("Report type does not exist");}
    	
        if(auditScore<100){
        	builder.setOverall_remarks(remarks).setNeed(1, 0, 0);
        	OpenReport report = (OpenReport) builder.build();
        	if(!builder.saveReport(report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}
        } else {
        	builder.setOverall_remarks(remarks).setOverall_statusAsClosed();
        	ClosedReport report = (ClosedReport) builder.build();
        	if(!builder.saveReport(report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}
        }
        logger.info("Report Submission Upload Completed.");
    	return ResponseEntity.ok(auditScore);
	}
	
	@PostMapping("/report/postReportUpdate")
	public ResponseEntity<?> postReportUpdate(
			@RequestParam(value = "report_id", required = true) int report_id,
            @RequestPart(value = "entry", required = true) String strEntry,
            @RequestParam(value = "remarks", required = true) String remarks,
            @RequestParam(value = "group_update", required = false, defaultValue = "false") boolean group_update){
		logger.info("Update of report of id " + report_id + " requested.");
		ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ReportEntry.class, new CustomReportEntryDeserializer());
        objectMapper.registerModule(module);
        JavaType customClassCollection = objectMapper.getTypeFactory()
        		.constructCollectionType(List.class, ReportEntry.class);
		List<ReportEntry> entries = new ArrayList<>();
		if(group_update) {
	        try {
	            List<ReportEntry> tentries = objectMapper.readValue(strEntry, customClassCollection);
	            entries = new ArrayList<>(tentries);
	        } catch (JsonProcessingException e) {
	            logger.warn("JSON PROCESSING EXCEPTION {} POST");
	            return ResponseEntity.badRequest().body(null);
	        }
		}else {
			try {
				ReportEntry entry = objectMapper.readValue(strEntry, ReportEntry.class);
				entries.add(entry);
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body("Entry malformed!");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body("Entry malformed!");
			}	
		}

		ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo,
				completedAuditRepo, report_id);
		
		if(builder == null) {
			return ResponseEntity.badRequest().body("Report not found.");
		}
		if(builder.getReportType().matches(ResourceString.REPORT_STATUS_CLOSED)) {
			return ResponseEntity.badRequest().body("Error! This report is already closed.");
		}
		
		for(ReportEntry entry:entries) {
			builder.addEntry(entry);
		}

		int auditScore = (int) builder.markReport(auditCheckListFBRepo, auditCheckListNFBRepo);
        if(auditScore<100){
        	builder.setOverall_remarks(remarks).setNeed(1, 0, 0);
        	OpenReport updated_report = (OpenReport) builder.build();
        	if(!builder.saveReport(updated_report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}
        } else {
        	builder.setOverall_remarks(remarks).setOverall_statusAsClosed();
        	ClosedReport updated_report = (ClosedReport) builder.build();
        	if(!builder.saveReport(updated_report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}else {
        		builder.deleteOpenReport(report_id);
        	}
        }
        logger.info("Report update completed.");
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
			//I've left it here just to catch bad reports
			@SuppressWarnings("unused")
			String reportJSON = objectmapper.writeValueAsString(report);
			return ResponseEntity.ok(report);
		} catch (JsonProcessingException e) {
			logger.error("MALFORMED REPORT!");
			return ResponseEntity.unprocessableEntity().build();
		}
	}
	
	//Is this really necessary? It seems enveloped by the method above
	@GetMapping("/report/getReportStatistics")
	public ResponseEntity<?> getReportStatistics(@RequestParam(required=true) int report_id){
		ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo, completedAuditRepo, report_id);
		if(builder == null) {
			return ResponseEntity.notFound().build();
		}
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode jNode = objectMapper.createObjectNode();
		
		//Get failed entries
		List<Integer> failed_entry_ids = new ArrayList<>();
		for(ReportEntry entry:builder.getEntries()) {
			if(entry.getStatus()==Component_Status.FAIL) {
				failed_entry_ids.add(entry.getEntry_id());
			}
		}
		
		try {
			jNode.put("Failed_Entries", objectMapper.writeValueAsString(failed_entry_ids));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Get score
		jNode.put("Score", builder.getOverall_score());
		
		JsonNode statistics = jNode;
		
		return ResponseEntity.ok(statistics);
		
	}
	
	@GetMapping("/report/getReportEntry")
	public ResponseEntity<?> getReportEntry(
			@RequestParam(required=true) int report_id, 
			@RequestParam(required=true) int entry_id){
		ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo, completedAuditRepo, report_id);
		if(builder == null) {
			return ResponseEntity.notFound().build();
		}
		ReportEntry entry = builder.getEntries().get(entry_id);
		
		return ResponseEntity.ok(entry);
	}
	
	@GetMapping("/report/getReportIDs")
	public ResponseEntity<?> getReportIDs(
			@RequestParam(required=false, defaultValue="-1") String username,
			@RequestParam(required=false, defaultValue="-1") int user_id,
			@RequestParam(required=false, defaultValue="ALL") String type
			){
		//error checking
		if(user_id == -1 && username.matches("-1")) {
			return ResponseEntity.badRequest().body("No user specified!");
		}
		AccountModel user = null;
		if(!username.matches("-1")) {//username provided
			user = accountRepo.findByUsername(username);
			int found_user_id = user.getAccount_id();
			if(user_id == -1) {
				user_id = found_user_id;
			}
			if(user_id != found_user_id) {
				return ResponseEntity.badRequest().body("Username and user_id do not match!");
			}
		}else {//username not provided
			user = accountRepo.findByAccId(user_id);
		}
		
		if(user == null) {
			return ResponseEntity.badRequest().body("User not found!");
		}
		
		//From here onwards, user_id can be relied upon
		String user_role = user.getRole_id();
		JsonNode report_ids = null;
		if(user_role.matches(ResourceString.TENANT_ROLE_KEY)) {
			report_ids = getTenantReportIds(user_id, type);
		}else if(user_role.matches(ResourceString.AUDITOR_ROLE_KEY)) {
			report_ids = getAuditorReportIds(user_id, type);
		}else if(user_role.matches(ResourceString.MANAGER_ROLE_KEY)) {
			//TODO
		}else {
			return ResponseEntity.unprocessableEntity().body(ResourceString.UNEXPECTED_ERR_MSG);
		}
		
		return ResponseEntity.ok(report_ids);
	}
	
	private JsonNode getTenantReportIds(int tenant_id, String type) {
		TenantModel tenant = tenantRepo.getTenantById(tenant_id);
		ObjectMapper objectmapper = new ObjectMapper();
		ObjectNode report_ids = objectmapper.createObjectNode();
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
				|| type.matches(ResourceString.GETREPORT_FILTER_CLOSED)) {
			report_ids.put(type, tenant.getPast_audits());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
				|| type.matches(ResourceString.GETREPORT_FILTER_LATEST)) {
			report_ids.put(type, tenant.getLatest_audit());
		}
		return report_ids;
	}
	
	private JsonNode getAuditorReportIds(int auditor_id, String type) {
		AuditorModel auditor = auditorRepo.getAuditorById(auditor_id);
		ObjectMapper objectmapper = new ObjectMapper();
		ObjectNode report_ids = objectmapper.createObjectNode();
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
				|| type.matches(ResourceString.GETREPORT_FILTER_CLOSED)) {
			report_ids.put(type, auditor.getCompleted_audits());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
				|| type.matches(ResourceString.GETREPORT_FILTER_OPEN)) {
			report_ids.put(type, auditor.getOutstanding_audit_ids());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
				|| type.matches(ResourceString.GETREPORT_FILTER_APPEALED)) {
			report_ids.put(type, auditor.getAppealed_audits());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
				|| type.matches(ResourceString.GETREPORT_FILTER_OVERDUE)) {
			//TODO
		}
		return report_ids;
	}
	
	@GetMapping("/report/print")
	public ResponseEntity<?> printURLRequest(HttpServletRequest request){
		String strRequest = request.getRequestURL().toString() + "?" + request.getQueryString();
		String strRequest2 = request.getParameterNames().toString();
		logger.info(strRequest);
		logger.info(strRequest2);
		return ResponseEntity.ok(strRequest + "<><>" + strRequest2);
	}

	@PostMapping("/report/print")
	public ResponseEntity<?> printURLRequest(HttpServletRequest request){
		String strRequest = request.getRequestURL().toString() + "?" + request.getQueryString();
		String strRequest2 = request.getParameterNames().toString();
		logger.info(strRequest);
		logger.info(strRequest2);
		return ResponseEntity.ok(strRequest + "<><>" + strRequest2);
	}

	
	
	
	
	
	
	
	
}
