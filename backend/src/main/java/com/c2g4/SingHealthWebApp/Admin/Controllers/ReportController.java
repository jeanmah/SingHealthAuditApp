package com.c2g4.SingHealthWebApp.Admin.Controllers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.c2g4.SingHealthWebApp.Admin.Models.*;
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
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListSMARepo;
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
import com.fasterxml.jackson.databind.node.ArrayNode;
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
	AuditCheckListSMARepo auditCheckListSMARepo;
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
			questions = new ArrayList<AuditCheckListModel>(auditCheckListSMARepo.getAllQuestions());
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
			question = auditCheckListSMARepo.getQuestion(qn_id);
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
            for(ReportEntry entry: entryList){
            	entry.setFrom_account_id(auditor_id);
			}
        } catch (JsonProcessingException e) {
            logger.warn("JSON PROCESSING EXCEPTION {} POST",report_type);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
        
        ReportBuilder builder = ReportBuilder.getNewReportBuilder(openAuditRepo, completedAuditRepo);
        builder.setUserIDs(tenant_id, auditor_id, manager_id).setEntries(entryList);
        builder.setReportType(report_type);
        
        int auditScore = (int) builder.markReport(auditCheckListFBRepo, auditCheckListNFBRepo, auditCheckListSMARepo);
    	if(auditScore == -1) {return ResponseEntity.badRequest().body("Report type does not exist");}
    	
        if(auditScore<100){
        	builder.setOverall_remarks(remarks).setNeed(1, 0, 0);
        	OpenReport report = (OpenReport) builder.build();
        	if(!builder.saveReport(report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}
			builder.updateLatestReportIds(report,tenantRepo,auditorRepo,managerRepo);
        } else {
			builder.setOverall_remarks(remarks).setNeed(0, 1, 0);
			Report report = builder.build();
        	if(!builder.saveImmediatelyCompletedReport(report, tenantRepo, auditorRepo, managerRepo)) {
                return ResponseEntity.badRequest().body(null);
        	}
        }

        tenantRepo.updateAuditScoreByTenantId(tenant_id,auditScore);
        logger.info("Report Submission Upload Completed.");
    	return ResponseEntity.ok(auditScore);
	}
	
	@PostMapping("/report/postReportUpdate")
	public ResponseEntity<?> postReportUpdate(
			@AuthenticationPrincipal UserDetails callerUser,
			@RequestParam(value = "report_id", required = true) int report_id,
            @RequestPart(value = "entry", required = true) String strEntry,
            @RequestParam(value = "remarks", required = true) String remarks,
            @RequestParam(value = "group_update", required = false, defaultValue = "false") boolean group_update){
		AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
		if (callerAccount==null) return ResponseEntity.badRequest().body(null);

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
				for(ReportEntry entry: entries){
					entry.setFrom_account_id(callerAccount.getAccount_id());
				}
	        } catch (JsonProcessingException e) {
	            logger.warn("JSON PROCESSING EXCEPTION {} POST");
	            return ResponseEntity.badRequest().body(null);
	        }
		}else {
			try {
				ReportEntry entry = objectMapper.readValue(strEntry, ReportEntry.class);
				logger.info("UPDATE QUESTION SET FROM ID {}",callerAccount.getAccount_id());
				entry.setFrom_account_id(callerAccount.getAccount_id());
				entries.add(entry);
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body("Entry malformed!");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body("Entry malformed!");
			}	
		}

		if(callerAccount.getRole_id().equals(ResourceString.TENANT_ROLE_KEY)){
			if(!checkTenantEntryPassFail(entries)){
				return ResponseEntity.badRequest().body("Tenant entry status should be fail");
			}
		}

		ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo,
				completedAuditRepo, report_id);
		
		if(builder == null) {
			return ResponseEntity.badRequest().body("Report not found.");
		}
//		if(builder.getReportType().matches(ResourceString.REPORT_STATUS_CLOSED)) {
//			return ResponseEntity.badRequest().body("Error! This report is already closed.");
//		}
		if(builder.getOverall_status()==1) {
			return ResponseEntity.badRequest().body("Error! This report is already closed.");
		}
		
		for(ReportEntry entry:entries) {
			builder.addEntry(entry);
		}
		int initialScore = builder.getOverall_score();
		int auditScore = (int) builder.markReport(auditCheckListFBRepo, auditCheckListNFBRepo, auditCheckListSMARepo);
        builder.setOverall_score(initialScore);
		if(auditScore<100){
			if(callerAccount.getRole_id().equals(ResourceString.TENANT_ROLE_KEY)){
				builder.setOverall_remarks(remarks).setNeed(0, 1, 0);
			} else {
				builder.setOverall_remarks(remarks).setNeed(1, 0, 0);
			}
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
        		builder.deleteOpenAuditsFromUsers(updated_report, tenantRepo, auditorRepo, managerRepo);
        		builder.updateLatestReportIds(updated_report,tenantRepo,auditorRepo,managerRepo);
        	}
        }
        logger.info("Report update completed.");
    	return ResponseEntity.ok(auditScore);
	}

	private boolean checkTenantEntryPassFail(List<ReportEntry> entries){
		for(ReportEntry reportEntry : entries){
			if(reportEntry.getStatus()!=Component_Status.FAIL) return false;
		}
		return true;
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
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode reportWithStoreName = addTenantStoreToReturnJson(objectmapper.writeValueAsString(report),report.getTenant_id());
			return ResponseEntity.ok(reportWithStoreName);
		} catch (JsonProcessingException e) {
			logger.error("MALFORMED REPORT!");
			return ResponseEntity.unprocessableEntity().build();
		}
	}

	private JsonNode addTenantStoreToReturnJson(String currentReturnString, int tenant_id) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode currentReturnNode = (ObjectNode)objectMapper.readTree(currentReturnString);
		currentReturnNode.put("store_name", tenantRepo.getStoreNameById(tenant_id));
		return currentReturnNode;
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
		ArrayNode failed_entry_ids = objectMapper.createArrayNode();
		for(ReportEntry entry:builder.getEntries()) {
			if(entry.getStatus()==Component_Status.FAIL) {
				failed_entry_ids.add(entry.getEntry_id());
			}
		}

		jNode.put("Failed_Entries", failed_entry_ids);

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
		ReportEntry entry = null;
		for(ReportEntry reportEntry: builder.getEntries()){
			if(reportEntry.getEntry_id() ==entry_id){
				entry = reportEntry;
			}
		}
		if (entry == null) {
			logger.warn("entry not found");
			return ResponseEntity.badRequest().body("entry "+entry_id+" not found");
		}

		ObjectNode entryOutput = addAdditionalEntryFields(entry,builder.getReportType());
		return ResponseEntity.ok(entryOutput);
	}

	private ObjectNode addAdditionalEntryFields(ReportEntry entry, String reportType){
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode entryOutput = objectMapper.valueToTree(entry);
		String requirement = "";
		if(reportType.equals(ResourceString.FB_KEY)){
			AuditCheckListFBModel auditCheckListModel = auditCheckListFBRepo.getQuestion(entry.getQn_id());
			requirement = auditCheckListModel.getRequirement();
		} else if(reportType.equals(ResourceString.NFB_KEY)){
			AuditCheckListNFBModel auditCheckListModel = auditCheckListNFBRepo.getQuestion(entry.getQn_id());
			requirement = auditCheckListModel.getRequirement();
		} else {
			AuditCheckListSMAModel auditCheckListModel = auditCheckListSMARepo.getQuestion(entry.getQn_id());
			requirement = auditCheckListModel.getRequirement();
		}
		entryOutput.put("Requirement",requirement);
		return entryOutput;
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
			report_ids.put(ResourceString.GETREPORT_FILTER_CLOSED, tenant.getPast_audits());
			logger.info("past audit tenant {}", tenant.getPast_audits().asText());

		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
				|| type.matches(ResourceString.GETREPORT_FILTER_LATEST)) {
			report_ids.put(ResourceString.GETREPORT_FILTER_LATEST, tenant.getLatest_audit());
			logger.info("latest audit tenant {}", tenant.getLatest_audit());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL)
				|| type.matches(ResourceString.GETREPORT_FILTER_OVERDUE)) {
			ArrayNode outstandingAuditIds = objectmapper.createArrayNode();
			outstandingAuditIds.add(tenant.getLatest_audit());
			report_ids.put(ResourceString.GETREPORT_FILTER_OVERDUE, getOverDueAudits(outstandingAuditIds));
		}
		return report_ids;
	}
	
	private JsonNode getAuditorReportIds(int auditor_id, String type) {
		AuditorModel auditor = auditorRepo.getAuditorById(auditor_id);

		ObjectMapper objectmapper = new ObjectMapper();
		ObjectNode report_ids = objectmapper.createObjectNode();
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
			  || type.matches(ResourceString.GETREPORT_FILTER_CLOSED)) {
			 report_ids.put(ResourceString.GETREPORT_FILTER_CLOSED, auditor.getCompleted_audits());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
			  || type.matches(ResourceString.GETREPORT_FILTER_OPEN)) {
			 report_ids.put(ResourceString.GETREPORT_FILTER_OPEN, auditor.getOutstanding_audit_ids());
			 logger.info("outstanding {}", auditor.getOutstanding_audit_ids());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
			  || type.matches(ResourceString.GETREPORT_FILTER_APPEALED)) {
			 report_ids.put(ResourceString.GETREPORT_FILTER_APPEALED, auditor.getAppealed_audits());
		}
		if(type.matches(ResourceString.GETREPORT_FILTER_ALL) 
			  || type.matches(ResourceString.GETREPORT_FILTER_OVERDUE)) {
			 ArrayNode outstandingAuditIds = (ArrayNode) auditor.getOutstanding_audit_ids()
			   .get(ResourceString.AUDITOR_OUTSTANDING_AUDITS_JSON_KEY);
			 report_ids.put(ResourceString.GETREPORT_FILTER_OVERDUE, getOverDueAudits(outstandingAuditIds));
		}
		return report_ids;
	 }

	private ArrayNode getOverDueAudits(ArrayNode outstandingAuditIds){
		ObjectMapper objectmapper = new ObjectMapper();
		ArrayNode overdueAudits = objectmapper.createArrayNode();
		for(int i =0;i<outstandingAuditIds.size();i++){
			ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo,
					completedAuditRepo, outstandingAuditIds.get(i).asInt());
			List<ReportEntry> overDueEntries = builder.getOverDueEntries();
			if(overDueEntries.size()==0){
				logger.info("outstanding audit {} is not overdue", outstandingAuditIds.get(i).asInt());
				continue;
			}
			overdueAudits.add(outstandingAuditIds.get(i).asInt());
		}
		return overdueAudits;
	}
	


	@PostMapping("/report/print")
	public ResponseEntity<?> printURLRequest(HttpServletRequest request){
		String strRequest = request.getRequestURL().toString() + "?" + request.getQueryString();
		String strRequest2 = request.getParameterNames().toString();
		logger.info(strRequest);
		logger.info(strRequest2);
		return ResponseEntity.ok(strRequest + "<><>" + strRequest2);
	}

	@GetMapping("/report/getRectificationEntryOfQn")
	public ResponseEntity<?> getRectificationEntryOfQn(@RequestParam int report_id,
													   @RequestParam int tenant_id,
													   @RequestParam int qn_id){
		ObjectMapper objectMapper = new ObjectMapper();
		ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo, completedAuditRepo, report_id);
		if(builder == null) {
			return ResponseEntity.notFound().build();
		}
		List<ReportEntry> entries = new ArrayList<>();

		for(ReportEntry reportEntry: builder.getEntries()){
			logger.info("Entry qn {} from account{}",reportEntry.getQn_id(),reportEntry.getFrom_account_id());
			if(reportEntry.getQn_id() ==qn_id && reportEntry.getFrom_account_id() == tenant_id){
				entries.add(reportEntry);
			}
		}
		ObjectNode root = objectMapper.createObjectNode();
		ArrayNode entriesArrayNode = objectMapper.createArrayNode();
		if(entries.size()==0){
			root.put("hasRectification",false);
			root.put("entries", entriesArrayNode);
		} else {
			root.put("hasRectification",true);
			for(ReportEntry re: entries){
				ObjectNode entryOutput = addAdditionalEntryFields(re,builder.getReportType());
				entriesArrayNode.add(entryOutput);
			}
			root.put("entries", entriesArrayNode);
		}
		return ResponseEntity.ok(root);
	}



	private AccountModel convertUserDetailsToAccount(UserDetails callerUser){
		logger.info("CALLER USER USERNAME {}",callerUser.getUsername());
		return accountRepo.findByUsername(callerUser.getUsername());
	}

	
	
}
