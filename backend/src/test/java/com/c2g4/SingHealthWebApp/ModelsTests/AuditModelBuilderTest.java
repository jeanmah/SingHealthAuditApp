package com.c2g4.SingHealthWebApp.ModelsTests;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditModelBuilder;
import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class AuditModelBuilderTest {

    private final String REPORTID = "REPORTID" ;
    private final String TENANTID = "TENANTID";
    private final String AUDITORID = "AUDITORID";
    private final String MANAGERID = "MANAGERID";
    //Dates
    private final String STARTDATE = "STARTDATE";
    private final String LASTUPDATEDATE = "LASTUPDATEDATE";
    private final String ENDDATE = "ENDDATE";
    //Results, Status and Data
    private final String OVERALLSCORE = "OVERALLSCORE";
    private final String OVERALLSTATUS= "OVERALLSTATUS";
    private final String OVERALLREMARKS ="OVERALLREMARKS";
    private final String REPORTTYPE = "REPORTTYPE";
    private final String REPORTDATA = "REPORTDATA";
    //Follow-up (if necessary)
    private final String NEEDTANTNT = "NEEDTANTNT";
    private final String NEEDAUDITOR = "NEEDAUDITOR";
    private final String NEEDMANAGER = "NEEDMANAGER";

    public static AuditModelBuilder auditModelBuilder;

    @BeforeEach
    public void beforeFunc(){
        auditModelBuilder = new AuditModelBuilder();
    }

    @Test
    public void createEmptyAuditBuilder(){
        assertAuditModelBuilder(new HashMap<String,String>());
    }

    @Test
    public void setUserIDs(){
        auditModelBuilder.setUserIDs(1,2,3);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(TENANTID,"1");
        editedVals.put(AUDITORID,"2");
        editedVals.put(MANAGERID,"3");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeed(){
        auditModelBuilder.setNeed(1,1,0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(NEEDTANTNT,"1");
        editedVals.put(NEEDAUDITOR,"1");
        editedVals.put(NEEDMANAGER,"0");
        assertAuditModelBuilder(editedVals);
    }
    @Test
    public void setTypeIsOpenAudit() {
        auditModelBuilder.setTypeIsOpenAudit();
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLSTATUS,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setTypeIsCompletedAudit() {
        auditModelBuilder.setTypeIsCompletedAudit();
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLSTATUS,"1");
        assertAuditModelBuilder(editedVals);
    }
    @Test
    public void setReportId() {
        auditModelBuilder.setReportId(1);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(REPORTID,"1");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeedTenant() {
        auditModelBuilder.setNeedTenant(1);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(NEEDTANTNT,"1");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeedAuditor() {
        HashMap<String,String> editedVals = new HashMap<>();
        auditModelBuilder.setNeedAuditor(1);
        editedVals.put(NEEDAUDITOR,"1");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeedManager() {
        auditModelBuilder.setNeedManager(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(NEEDMANAGER,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setTenantId() {
        auditModelBuilder.setTenantId(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(TENANTID,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setAuditorId() {
        auditModelBuilder.setAuditorId(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(AUDITORID,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setManagerId() {
        auditModelBuilder.setManagerId(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(MANAGERID,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setReport_typeOpen() {
        auditModelBuilder.setReport_type("Open Audit");
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(REPORTTYPE,"Open Audit");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setReport_typeCompleted() {
        auditModelBuilder.setReport_type("Completed Audit");
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(REPORTTYPE,"Completed Audit");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setReport_typeBAD() {
        try {
            auditModelBuilder.setReport_type("BAD");
        }catch (IllegalArgumentException e) {
            System.out.println("OK");
            return;
        }
        fail();
    }

    @Test
    public void setOverallRemarks() {
        auditModelBuilder.setOverallRemarks("REMARKS");
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLREMARKS,"REMARKS");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setOverallScore() {
        auditModelBuilder.setOverallScore(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLSCORE,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setReportDataString() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key","value");
        HashMap<String,String> editedVals = new HashMap<>();
        try {
            String nodeString = objectMapper.writeValueAsString(node);
            auditModelBuilder.setReportData(nodeString);
            editedVals.put(REPORTDATA,nodeString);
            assertAuditModelBuilder(editedVals);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void setReportDataJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key","value");
        HashMap<String,String> editedVals = new HashMap<>();
        auditModelBuilder.setReportData(node);
        try {
            editedVals.put(REPORTDATA,objectMapper.writeValueAsString(node));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
        assertAuditModelBuilder(editedVals);
    }

/*
    public String getOverallRemarks() {
        return overall_remarks;
    }

    public String getReport_type() {
        return report_type;
    }



    public int getOverallScore() {
        return overall_score;
    }
*/

    @Test
    public void getTenantId() {
        int id = 1;
        auditModelBuilder.setTenantId(id);
        assert (auditModelBuilder.getTenantId()==id);
    }
    @Test
    public void getAuditorId() {
        int id = 1;
        auditModelBuilder.setAuditorId(id);
        assert (auditModelBuilder.getAuditorId()==id);
    }

    @Test
    public void getManagerId() {
        int id = 1;
        auditModelBuilder.setManagerId(id);
        assert (auditModelBuilder.getManagerId()==id);
    }

    @Test
    public void getReportData(JsonNode report_data) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key","value");
        HashMap<String,String> editedVals = new HashMap<>();

        auditModelBuilder.setReportData(node);
        assert(auditModelBuilder.getReportData().has("key"));
        assert(auditModelBuilder.getReportData().get("key") == node.get("key"));
    }

    @Test
    public void getReportId() {
        int reportID = 1;
        auditModelBuilder.setReportId(reportID);
        assert (auditModelBuilder.getReportId()==reportID);
    }

    @Test
    public void getNeedManager(){
        int need = 1;
        auditModelBuilder.setNeedManager(need);
        assert (auditModelBuilder.getNeedManager()==need);
    }

    @Test
    public void getNeedAuditor(){
        int need = 1;
        auditModelBuilder.setNeedAuditor(need);
        assert (auditModelBuilder.getNeedAuditor()==need);
    }

    @Test
    public void getNeedTenant(){
        int need = 1;
        auditModelBuilder.setNeedTenant(need);
        assert (auditModelBuilder.getNeedTenant()==need);
    }


    private void assertAuditModelBuilder(HashMap<String,String> editedValues) {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,String> defaultValues = new HashMap<>(){{
            put(REPORTID,"-1");
            put(TENANTID,"-1");
            put(AUDITORID,"-1");
            put(MANAGERID,"-1");
            put(OVERALLSCORE,"-1");
            put(OVERALLREMARKS,"Nil");
            put(OVERALLSTATUS,"-1");
            put(REPORTTYPE,"-1");
            put(REPORTDATA,null);
            put(STARTDATE,null);
            put(LASTUPDATEDATE,null);
            put(ENDDATE,null);
            put(NEEDTANTNT,"-1");
            put(NEEDAUDITOR,"-1");
            put(NEEDMANAGER,"-1");
        }};

        for(String editedKey: editedValues.keySet()){
            defaultValues.put(editedKey,editedValues.get(editedKey));
        }

        assert(auditModelBuilder.getReportId()==Integer.parseInt(defaultValues.get(REPORTID)));
        assert(auditModelBuilder.getTenantId()==Integer.parseInt(defaultValues.get(TENANTID)));
        assert(auditModelBuilder.getAuditorId()==Integer.parseInt(defaultValues.get(AUDITORID)));
        assert(auditModelBuilder.getManagerId()==Integer.parseInt(defaultValues.get(MANAGERID)));
        assert(auditModelBuilder.getOverallRemarks().equals(defaultValues.get(OVERALLREMARKS)));
        assert(auditModelBuilder.getReport_type().equals(defaultValues.get(REPORTTYPE)));
        //assert(auditModelBuilder.getOverallScore()==Integer.parseInt(defaultValues.get(OVERALLSTATUS)));
        assert(auditModelBuilder.getOverallScore()==Integer.parseInt(defaultValues.get(OVERALLSCORE)));
        if(defaultValues.get(REPORTDATA)==null) {
            assert (auditModelBuilder.getReportData() == null);
        } else {
            try {
                assert (objectMapper.writeValueAsString(auditModelBuilder.getReportData()).equals(defaultValues.get(REPORTDATA)));
            } catch (JsonProcessingException e){
                fail();
            }
        }
        assert(auditModelBuilder.getNeedTenant()==Integer.parseInt(defaultValues.get(NEEDTANTNT)));
        assert(auditModelBuilder.getNeedAuditor()==Integer.parseInt(defaultValues.get(NEEDAUDITOR)));
        assert(auditModelBuilder.getNeedManager()==Integer.parseInt(defaultValues.get(NEEDMANAGER)));

        if(editedValues.containsKey(STARTDATE)){
            assert (auditModelBuilder.getStart_date().toString().equals(defaultValues.get(STARTDATE)));
        }
        if(editedValues.containsKey(ENDDATE)){
            assert (auditModelBuilder.getEnd_date().toString().equals(defaultValues.get(ENDDATE)));
        }
        if(editedValues.containsKey(LASTUPDATEDATE)){
            assert (auditModelBuilder.getLastUpdateDate().toString().equals(defaultValues.get(LASTUPDATEDATE)));
        }
    }

}
/*
    public AuditModel build() {
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
        if (this.report_type.matches("-1")) {
            logger.error("report_type not set!");
            throw new IllegalArgumentException();
        }
        if (this.report_data == null) {
            logger.error("There is no report data!");
            throw new IllegalArgumentException();
        }
        if (this.overall_status == 0 && (this.need_auditor + this.need_tenant + this.need_manager < 1)) {
            logger.error("This report is open but no 'need_user' bit has been set!");
            throw new IllegalArgumentException();
        }

        AuditModel audit = null;
        switch(this.overall_status) {
            case 0:
                audit = new OpenAuditModel(this.report_id, this.tenant_id, this.auditor_id,
                        this.manager_id, this.start_date, this.last_update_date, this.overall_remarks,
                        this.report_type, this.overall_score, this.report_data,
                        this.need_tenant, this.need_auditor,this.need_manager);
                break;
            case 1:
                audit = new CompletedAuditModel(this.report_id, this.tenant_id, this.auditor_id,
                        this.manager_id, this.start_date, this.end_date, this.overall_remarks,
                        this.report_type, this.overall_score, this.report_data);
                break;
        }
        return audit;
    }

    public AuditModelBuilder initTestOpenAudit() {
        setReportId(0);
        setTenantId(1);
        setAuditorId(2);
        setManagerId(3);
        setOverallRemarks("Test OpenAuditModel");
        setOverallScore(4);
        ObjectMapper objectmapper = new ObjectMapper();
        try {
            this.report_data = objectmapper.readTree("{name: \"John\", age: 31, city: \"New York\"}");
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.need_auditor = 1;
        this.overall_status = 0;
        return this;
    }


    public  void initTestCompletedAudit() {
        setReportId(0);
        setTenantId1
        setAuditorId(2);
        setManagerId3;
        setOverallRemarks("Test CompletedAuditModel");
        serreportytp(fbkey)_
                setOverallScore(4);

        try {
            this.repordatt = obj.readTree("{name: \"John\", age: 31, city: \"New York\"}");
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.overall_status = 1;
        return this;
    }

    //Getters and Setters for the builder class specifically
    public String getReportType() {
        switch(this.overall_status) {
            case 0:
                return "Open Audit";
            case 1:
                return "Completed Audit";
        }
        return null;
    }


    public Date getStart_date() {
        return start_date;
    }

    public AuditModelBuilder setStartDate(Date start_date) {
        this.start_date = start_date;
        return this;
    }

    public Date getLastUpdateDate() {
        return last_update_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public AuditModelBuilder setEnd_date(Date end_date) {
        this.end_date = end_date;
        return this;
    }

    public AuditModelBuilder setLastUpdateDate(Date last_update_date) {
        this.last_update_date = last_update_date;
        return this;
    }
*/



