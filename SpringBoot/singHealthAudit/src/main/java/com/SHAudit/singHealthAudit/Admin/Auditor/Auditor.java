package com.SHAudit.singHealthAudit.Admin.Auditor;

import com.mysql.cj.xdevapi.JsonString;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;

@Table("Auditors")
@TypeDef(name = "json", typeClass = JsonStringType.class)

public class Auditor {

    @Id
    private int acc_id;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String completed_audits;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String appealed_audits;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String outstanding_audit_ids;
    private String branch_id;
    private int mgr_id;

    public int getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(int acc_id) {
        this.acc_id = acc_id;
    }

    public String getCompleted_audits() {
        return completed_audits;
    }

    public void setCompleted_audits(String completed_audits) {
        this.completed_audits = completed_audits;
    }

    public String getAppealed_audits() {
        return appealed_audits;
    }

    public void setAppealed_audits(String appealed_audits) {
        this.appealed_audits = appealed_audits;
    }


    public String getOutstanding_audit_ids() {
        return outstanding_audit_ids;
    }

    public void setOutstanding_audit_ids(String outstanding_audit_ids) {
        this.outstanding_audit_ids = outstanding_audit_ids;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public int getMgr_id() {
        return mgr_id;
    }

    public void setMgr_id(int mgr_id) {
        this.mgr_id = mgr_id;
    }
}
