package com.SHAudit.singHealthAudit.Admin.Tenant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Tenant")
public class Tenant {
    @Id
    private int acc_id;
    private String type_id;
    private int audit_score;
    private int latest_audit;
    private int past_audits;
    private String branch_id;

    public int getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(int acc_id) {
        this.acc_id = acc_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public double getAudit_score() {
        return audit_score;
    }

    public void setAudit_score(int audit_score) {
        this.audit_score = audit_score;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }
}
