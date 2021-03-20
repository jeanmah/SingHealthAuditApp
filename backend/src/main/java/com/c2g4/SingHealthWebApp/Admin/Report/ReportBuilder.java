package com.c2g4.SingHealthWebApp.Admin.Report;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditModelBuilder;
import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListNFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * ReportBuilder serves as a builder for the ReportObject as well as provides utility functions
 * for the handling of ReportObjects.
 * @author LunarFox
 *
 */
public class ReportBuilder {
	//These to repos cannot be autowired from here, idk why it doesn't work
	//The workaround is to pass the auditRepo when calling the relevant methods
    private OpenAuditRepo openAuditRepo;
    private CompletedAuditRepo completedAuditRepo;
    private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
    private static ObjectMapper objectmapper = new ObjectMapper();
	
    //IDs
	private int report_id;
    private int tenant_id;
    private int auditor_id;
    private int manager_id;
    //Dates
    private Date open_date;
    private Date last_update_date;
    private Date close_date;
    //Results, Status and Data
    private int overall_score;
    private String overall_remarks;
    private List<ReportEntry> entries;
    //Follow-up (if necessary)
    private int need_tenant;
    private int need_auditor;
    private int need_manager;
    //0 means open, 1 means completed
    private int overall_status;
    
    //Constructors
    private ReportBuilder() {
		java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
		Date sqlCurrentDate = new Date(utilCurrentDate.getTime());
		this.report_id = -1;
		this.tenant_id = -1;
		this.auditor_id = -1;
		this.manager_id = -1;
		this.open_date = sqlCurrentDate;
		this.overall_remarks = "Nil";
		this.overall_score = -1;
		this.entries = new ArrayList<>();
		this.need_tenant = 0;
		this.need_auditor = 0;
		this.need_manager = 0;
		
		//Attributes specific to one of the classes
		this.last_update_date = sqlCurrentDate;
		this.close_date = sqlCurrentDate;
		
		this.overall_status = -1;
		
		this.completedAuditRepo = null;
		this.openAuditRepo = null;
    }
    
    private ReportBuilder(OpenAuditModel auditModel) {
    	this.report_id = auditModel.getReport_id();
    	this.tenant_id = auditModel.getTenant_id();
    	this.auditor_id = auditModel.getAuditor_id();
    	this.manager_id = auditModel.getManager_id();
    	this.open_date = auditModel.getStart_date();
    	this.overall_remarks = auditModel.getOverall_remarks();
    	this.overall_score = auditModel.getOverall_score();
    	try {
			this.entries = objectmapper.treeToValue(auditModel.getReport_data(), OpenReport.class).getEntries();
    	} catch (JsonProcessingException e) {
			logger.error("Could not wrap auditModel into Report due to malformed report_data!");
			e.printStackTrace();
		}
    	this.need_auditor = auditModel.getNeed_auditor();
    	this.need_tenant = auditModel.getNeed_tenant();
    	this.need_manager = auditModel.getNeed_manager();
    	this.last_update_date = auditModel.getLast_update_date();
    	this.overall_status = 0;
    }
    
    private ReportBuilder(CompletedAuditModel auditModel) {
    	this.report_id = auditModel.getReport_id();
    	this.tenant_id = auditModel.getTenant_id();
    	this.auditor_id = auditModel.getAuditor_id();
    	this.manager_id = auditModel.getManager_id();
    	this.open_date = auditModel.getStart_date();
    	this.overall_remarks = auditModel.getOverall_remarks();
    	this.overall_score = auditModel.getOverall_score();
    	try {
			this.entries = objectmapper.treeToValue(auditModel.getReport_data(), OpenReport.class).getEntries();
    	} catch (JsonProcessingException e) {
			logger.error("Could not wrap auditModel into Report due to malformed report_data!");
			e.printStackTrace();
		}
    	this.close_date = auditModel.getEnd_date();
    	this.overall_status = 1;
    }
    
    /**
     * Builds and returns a Report Object from the parameters contained in the ReportBuilder.
     * 
     * @return Either an OpenReport or a CloseReport object
     */
    
    public Report build() {
		//Check for errors
		if (this.report_id == -1) {
			logger.error("Report_id not set!");
			throw new IllegalArgumentException();
		}
		if (this.tenant_id == -1) {
			logger.error("Tenant_id not set!");
			throw new IllegalArgumentException();
		}
		if (this.auditor_id == -1) {
			logger.error("Auditor_id not set!");
			throw new IllegalArgumentException();
		}
		if (this.overall_score == -1) {
			logger.error("overall_score not set!");
			throw new IllegalArgumentException();
		}
		if (this.overall_status == -1) {
			logger.error("overall_status not set!");
			throw new IllegalArgumentException();
		}
		if (this.overall_status == 0 && (this.need_auditor + this.need_tenant + this.need_manager < 1)) {
			logger.error("This report is open but no 'need_user' bit has been set!");
			throw new IllegalArgumentException();
		}
		
		Report report = null;
		switch(this.overall_status) {
		case 0:
			report = new OpenReport(report_id, tenant_id, auditor_id, manager_id, open_date, overall_score, overall_remarks, entries, need_tenant,
					need_auditor, need_manager, overall_status, last_update_date);
			break;
		case 1:
			report = new ClosedReport(report_id, tenant_id, auditor_id, manager_id, open_date, overall_score, overall_remarks, entries, need_tenant,
					need_auditor, need_manager, overall_status, close_date);
			break;
		default:
			logger.error("Report is of an invalid type!");
			throw new IllegalArgumentException();
		}
		return report;
    	
    }
    
    //Methods for creating, modifying and loading reports
    /**
     * Returns a ReportBuilder for creating a custom Report Object.
     * @param openAuditRepo repo object for interfacing with the database
     * @param completedAuditRepo repo object for interfacing with the database
     * @return ReportBuilder object with default values.
     */
    public static ReportBuilder getReportBuilder(OpenAuditRepo openAuditRepo, CompletedAuditRepo completedAuditRepo) {
    	ReportBuilder builder = new ReportBuilder();
    	builder.setOpenAuditRepo(openAuditRepo);
    	builder.setCompletedAuditRepo(completedAuditRepo);
    	return builder;
    }
    
    /**
     * Returns a ReportBuilder for creating a new Report.
     * @param openAuditRepo repo object for interfacing with the database
     * @param completedAuditRepo repo object for interfacing with the database
     * @return ReportBuilder object with values set for creating a new OpenReport.
     */
    public static ReportBuilder getNewReportBuilder(OpenAuditRepo openAuditRepo, CompletedAuditRepo completedAuditRepo) {
    	ReportBuilder builder = new ReportBuilder();
    	builder.setReport_id(0);
    	builder.setOverall_statusAsOpen();
    	builder.setOpenAuditRepo(openAuditRepo);
    	builder.setCompletedAuditRepo(completedAuditRepo);
    	return builder;
    }

    /**
     * Closes an openreport into a closedreport.
     * @param report object to be converted into a closedreport
     * @return ClosedReport generated from the openreport object
     */
    public ClosedReport closeReport(OpenReport report) {
    	ReportBuilder builder = new ReportBuilder();
    	builder.setReport_id(report.getReport_id())
    	.setUserIDs(report.getTenant_id(), report.getAuditor_id(), report.getManager_id())
    	.setOpen_date(report.getOpen_date()).setOverall_remarks(report.getOverall_remarks())
    	.setOverall_score(report.getOverall_score()).setEntries(report.getEntries());
    	ClosedReport closedreport = (ClosedReport)builder.build();
    	return closedreport;
    }
    
    //TODO Implement a way to reopen a closedReport if necessary
    
    /**
     * Checks if an openreport by the given id exists in the database
     * @param report_id ID of the report
     * @return True if the report exists in the database and false otherwise
     */
    public boolean checkOpenReportExists(int report_id) {
    	return openAuditRepo.existsById((long)report_id);
    }
    
    //TODO Go track down why the above requires long but the below requires int
    //I've checked the db, the repos, the model, and the builder, idk why it's like this
    /**
     * Checks if a closedreport by the given id exists in the database
     * @param report_id ID of the report
     * @return True if the report exists in the database and false otherwise
     */
    public boolean checkClosedReportExists(int report_id) {
    	return completedAuditRepo.existsById(report_id);
    }
    
    /**
     * Returns the openreport by the given id from the database if it exists.
     * This method actually obtains an AuditModel from the db and converts it into
     * a Report object for returning.
     * @param report_id ID of the report
     * @return OpenReport of the given id
     */
    public OpenReport loadOpenReport(int report_id) {
    	OpenAuditModel auditModel = openAuditRepo.getOpenAuditById(report_id);
    	if (auditModel == null) {
    		logger.error("A report with the id " + report_id + " could not be found in the database.");
    		throw new IllegalArgumentException();
    	}else {
    		logger.info("Report with id " + report_id + " has been found.");
    	}
    	ReportBuilder builder = new ReportBuilder(auditModel);
    	return (OpenReport) builder.build();
    }
    
    /**
     * Returns the closedreport by the given id from the database if it exists.
     * This method actually obtains an AuditModel from the db and converts it into
     * a Report object for returning.
     * @param report_id ID of the report
     * @return ClosedReport of the given id
     */
    public ClosedReport loadClosedReport(int report_id) {
    	CompletedAuditModel auditModel = completedAuditRepo.getCompletedAuditById(report_id);
    	if (auditModel == null) {
    		logger.error("A report with the id " + report_id + " could not be found in the database.");
    		throw new IllegalArgumentException();
    	}else {
    		logger.info("Report with id " + report_id + " has been found.");
    	}
    	ReportBuilder builder = new ReportBuilder(auditModel);
    	return (ClosedReport) builder.build();
    }
    
    //Methods for saving reports
    private boolean saveOpenReport(OpenReport report) {
    	//Just to prevent any cheating/automate date updates ^_^
		java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
		Date sqlCurrentDate = new Date(utilCurrentDate.getTime());
		report.setLast_update_date(sqlCurrentDate);
		
    	AuditModelBuilder builder = new AuditModelBuilder();
        builder.setReportId(report.getReport_id()).setUserIDs(report.getTenant_id(), report.getAuditor_id()
        		, report.getManager_id()).setOverallRemarks(report.getOverall_remarks()).setOverallScore(report.getOverall_score())
        .setReportData(objectmapper.valueToTree(report)).setNeed(1,1,1)
        .setStartDate(report.getOpen_date()).setLastUpdateDate(report.getLast_update_date())
        .setTypeIsOpenAudit();
        
        OpenAuditModel audit = (OpenAuditModel) builder.build();
        try {
        	openAuditRepo.save(audit);
        }catch (IllegalArgumentException e) {
        	return false;
        }
        return true;
    }
    
    private boolean saveClosedReport(ClosedReport report) {
    	//Just to prevent any cheating/automate date updates ^_^
		java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
		Date sqlCurrentDate = new Date(utilCurrentDate.getTime());
		report.setClose_date(sqlCurrentDate);
		
    	AuditModelBuilder builder = new AuditModelBuilder();
        builder.setReportId(report.getReport_id()).setUserIDs(report.getTenant_id(), report.getAuditor_id()
        		, report.getManager_id()).setOverallRemarks(report.getOverall_remarks()).setOverallScore(report.getOverall_score())
        .setReportData(objectmapper.valueToTree(report)).setNeed(1,1,1)
        .setStartDate(report.getOpen_date()).setEnd_date(report.getClose_date())
        .setTypeIsCompletedAudit();
        
        CompletedAuditModel audit = (CompletedAuditModel) builder.build();
        try {
        	completedAuditRepo.save(audit);
        }catch (IllegalArgumentException e) {
        	return false;
        }
        return true;
    }
    
    /**
     * Saves a given report object into the database, regardless of its subclass.
     * This method abstracts away the need to convert the report into an AuditModel which is the
     * representation of the report in the database.
     * @param report object to be saved
     * @return True if the save was successfull, and false otherwise.
     */
    public boolean saveReport (Report report) {
    	if(report.getClass().equals(OpenReport.class)) {
    		return saveOpenReport((OpenReport)report);
    	}else if(report.getClass().equals(ClosedReport.class)) {
    		return saveClosedReport((ClosedReport)report);
    	}else {
    		logger.warn("Report is of an invalid type. Unable to save.");
    		return false;
    	}
    }
    
    //getters and setters
	public int getReport_id() {
		return report_id;
	}

	public ReportBuilder setReport_id(int report_id) {
		this.report_id = report_id;
		return this;
	}

	public int getTenant_id() {
		return tenant_id;
	}

	public ReportBuilder setTenant_id(int tenant_id) {
		this.tenant_id = tenant_id;
		return this;
	}

	public int getAuditor_id() {
		return auditor_id;
	}

	public ReportBuilder setAuditor_id(int auditor_id) {
		this.auditor_id = auditor_id;
		return this;
	}

	public int getManager_id() {
		return manager_id;
	}

	public ReportBuilder setManager_id(int manager_id) {
		this.manager_id = manager_id;
		return this;
	}

	public Date getOpen_date() {
		return open_date;
	}

	public ReportBuilder setOpen_date(Date open_date) {
		this.open_date = open_date;
		return this;
	}

	public Date getLast_update_date() {
		return last_update_date;
	}

	public ReportBuilder setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
		return this;
	}

	public Date getClose_date() {
		return close_date;
	}

	public ReportBuilder setClose_date(Date close_date) {
		this.close_date = close_date;
		return this;
	}

	public int getOverall_score() {
		return overall_score;
	}

	public ReportBuilder setOverall_score(int overall_score) {
		this.overall_score = overall_score;
		return this;
	}

	public String getOverall_remarks() {
		return overall_remarks;
	}

	public ReportBuilder setOverall_remarks(String overall_remarks) {
		this.overall_remarks = overall_remarks;
		return this;
	}

	public List<ReportEntry> getEntries() {
		return entries;
	}

	public ReportBuilder setEntries(List<ReportEntry> entries) {
		this.entries = entries;
		return this;
	}

	public int getNeed_tenant() {
		return need_tenant;
	}

	public ReportBuilder setNeed_tenant(int need_tenant) {
		this.need_tenant = need_tenant;
		return this;
	}

	public int getNeed_auditor() {
		return need_auditor;
	}

	public ReportBuilder setNeed_auditor(int need_auditor) {
		this.need_auditor = need_auditor;
		return this;
	}

	public int getNeed_manager() {
		return need_manager;
	}

	public ReportBuilder setNeed_manager(int need_manager) {
		this.need_manager = need_manager;
		return this;
		
	}

	public int getOverall_status() {
		return overall_status;
	}
	
	public ReportBuilder setOverall_statusAsOpen() {
		this.overall_status = 0;
		return this;
	}

	public ReportBuilder setOverall_statusAsClosed() {
		this.overall_status = 1;
		return this;
	}
	
	public OpenAuditRepo getOpenAuditRepo() {
		return openAuditRepo;
	}

	public void setOpenAuditRepo(OpenAuditRepo openAuditRepo) {
		this.openAuditRepo = openAuditRepo;
	}

	public CompletedAuditRepo getCompletedAuditRepo() {
		return completedAuditRepo;
	}

	public void setCompletedAuditRepo(CompletedAuditRepo completedAuditRepo) {
		this.completedAuditRepo = completedAuditRepo;
	}
	
	//Condensed Builders Setters
	
	public ReportBuilder setUserIDs(int tenant_id, int auditor_id, int manager_id) {
		this.tenant_id = tenant_id;
		this.auditor_id = auditor_id;
		this.manager_id = manager_id;
		return this;
	}
	
	public ReportBuilder setNeed(int need_tenant, int need_auditor, int need_manager) {
		this.need_tenant = need_tenant;
		this.need_auditor = need_auditor;
		this.need_manager = need_manager;
		return this;
	}
	
	//Start of logic for processing Report entries
	/**
	 * 
	 * @param report
	 * @param auditCheckListFBRepo
	 * @param auditCheckListNFBRepo
	 * @param images
	 * @param category
	 * @return
	 */
	public double markReport(Report report, AuditCheckListFBRepo auditCheckListFBRepo, AuditCheckListNFBRepo auditCheckListNFBRepo,
			MultipartFile[] images, String category) {
		return markEntries(report.getEntries(), auditCheckListFBRepo, auditCheckListNFBRepo, images, category);
	}
	
	public double markEntries(AuditCheckListFBRepo auditCheckListFBRepo, AuditCheckListNFBRepo auditCheckListNFBRepo,
			MultipartFile[] images, String category) {
		return markEntries(this.getEntries(), auditCheckListFBRepo, auditCheckListNFBRepo, images, category);
	}
	
	public double markEntries(List<ReportEntry> entries, AuditCheckListFBRepo auditCheckListFBRepo, AuditCheckListNFBRepo auditCheckListNFBRepo,
			MultipartFile[] images, String category) {
        HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap = new HashMap<>();
        int imageCounter = 0;
        
        //TODO: figure out if this conversion is gonna screw things up
        for(ReportEntry reportEntry: entries){
            AuditorReportEntry auditorReportEntry = (AuditorReportEntry) reportEntry;

            String qnCategory = category.equals("fbchecklist") ?
                    auditCheckListFBRepo.getCategoryByQnID(reportEntry.getQn_id())
                    : auditCheckListNFBRepo.getCategoryByQnID(reportEntry.getQn_id());

            if(auditorReportEntry.getStatus() == Component_Status.FAIL){
                MultipartFile uploadedImage = images[imageCounter];
                //No img checking
                if(uploadedImage == null) {
                    logger.warn("UPLOADED IMAGE NUM {} NULL CHECKLIST POST",imageCounter);
                    return -1;
                }
                
                //Save img as Base64
                try {
                    logger.warn("UPLOADED IMAGE NAME {} FBCHECKLIST POST",uploadedImage.getOriginalFilename());
                    String base64img = Base64.getEncoder().encodeToString(uploadedImage.getBytes());
                    reportEntry.addImage(base64img);
                    imageCounter++;
                } catch (IOException e) {
                    logger.warn("UPLOADED IMAGE NUM {} CANNOT OPEN FILE CHECKLIST POST",imageCounter);
                    return -1;
                }
                
                //Update Score
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertWrong();
                } else{
                    double weight = category.equals("fbchecklist") ?
                            auditCheckListFBRepo.getWeightByQnID(reportEntry.getQn_id())
                            : auditCheckListNFBRepo.getWeightByQnID(reportEntry.getQn_id());
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,weight,1,1);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
                
            } else{
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertCorrect();
                } else{
                    double weight = category.equals("fbchecklist") ?
                            auditCheckListFBRepo.getWeightByQnID(reportEntry.getQn_id())
                            : auditCheckListNFBRepo.getWeightByQnID(reportEntry.getQn_id());
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,weight,1,0);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
            }
        }
        String str_score = String.valueOf(calculateScore(checklistCategoryScoresHashMap));
        return Double.parseDouble(str_score) *100.0;
    }
	
    private double calculateScore(HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap){
        if(checklistCategoryScoresHashMap.isEmpty()) return 0;
        double totalScore = 0;
        for(ChecklistCategoryScores categoryScores: checklistCategoryScoresHashMap.values()){
            totalScore+= categoryScores.getScore();
        }
        return totalScore;
    }
}

class ChecklistCategoryScores{

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
