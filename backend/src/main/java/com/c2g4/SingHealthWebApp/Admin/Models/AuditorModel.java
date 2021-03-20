package com.c2g4.SingHealthWebApp.Admin.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.databind.JsonNode;

@Table("Auditors")

public class AuditorModel implements typeAccountModel{
	
	@Id
	private int acc_id;
	
	private JsonNode completed_audits;
	private JsonNode appealed_audits;
	private JsonNode outstanding_audit_ids;
	private String branch_id;
	private int mgr_id;

	@Override
	public int getAcc_id() {
		return acc_id;
	}
	public void setAcc_id(int acc_id) {
		this.acc_id = acc_id;
	}
	public JsonNode getCompleted_audits() {
		return completed_audits;
	}
	public void setCompleted_audits(JsonNode completed_audits) {
		this.completed_audits = completed_audits;
	}
	public JsonNode getAppealed_audits() {
		return appealed_audits;
	}
	public void setAppealed_audits(JsonNode appealed_audits) {
		this.appealed_audits = appealed_audits;
	}
	public JsonNode getOutstanding_audit_ids() {
		return outstanding_audit_ids;
	}
	public void setOutstanding_audit_ids(JsonNode outstanding_audit_ids) {
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
