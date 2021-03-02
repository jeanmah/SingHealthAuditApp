package com.SHAudit.singHealthAudit.Audit;
import com.mysql.cj.xdevapi.JsonString;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.sql.Date;

@Table("Completed_Audits")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CompletedAudits {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int report_id;
    private int tenant_id;
    private int auditor_id;
    private int manager_id;
    private Date start_date;
    private Date end_date;
    private String overall_remarks;
    private double overall_score;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String report_data;

    public CompletedAudits(int tenant_id, int auditor_id, int manager_id, Date start_date, Date end_date,
                           String overall_remarks, double overall_score, String report_data) {
        this.tenant_id = tenant_id;
        this.auditor_id = auditor_id;
        this.manager_id = manager_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.overall_remarks = overall_remarks;
        this.overall_score = overall_score;
        this.report_data = report_data;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public int getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(int tenant_id) {
        this.tenant_id = tenant_id;
    }

    public int getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(int auditor_id) {
        this.auditor_id = auditor_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getOverall_remarks() {
        return overall_remarks;
    }

    public void setOverall_remarks(String overall_remarks) {
        this.overall_remarks = overall_remarks;
    }

    public double getOverall_score() {
        return overall_score;
    }

    public void setOverall_score(double overall_score) {
        this.overall_score = overall_score;
    }

    public String getReport_data() {
        return report_data;
    }

    public void setReport_data(String report_data) {
        this.report_data = report_data;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }
}
