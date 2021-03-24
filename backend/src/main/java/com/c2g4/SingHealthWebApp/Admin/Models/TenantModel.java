package com.c2g4.SingHealthWebApp.Admin.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.databind.JsonNode;

@Table("Tenant")
public class TenantModel {
    @Id
    private int acc_id;
    private String type_id;
    private int audit_score;
    private int latest_audit;
    private JsonNode past_audits;
    private String branch_id;
    private String store_addr;
    
	public int getAcc_id() {
		return acc_id;
	}
	public void setAcc_id(int account_id) {
		this.acc_id = account_id;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public int getAudit_score() {
		return audit_score;
	}
	public void setAudit_score(int audit_score) {
		this.audit_score = audit_score;
	}
	public int getLatest_audit() {
		return latest_audit;
	}
	public void setLatest_audit(int latest_audit) {
		this.latest_audit = latest_audit;
	}
	public JsonNode getPast_audits() {
		return past_audits;
	}
	public void setPast_audits(JsonNode past_audits) {
		this.past_audits = past_audits;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	public String getStore_addr() {
		return store_addr;
	}
	public void setStore_addr(String store_addr) {
		this.store_addr = store_addr;
	}

    
    

}
