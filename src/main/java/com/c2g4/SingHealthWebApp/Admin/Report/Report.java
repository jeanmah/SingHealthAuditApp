package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.util.List;

public abstract class Report {
	
    protected int overall_score;
    protected boolean overall_status;
    protected Date open_date;
    protected List<ReportEntry> entries;
    
	public Report(int overall_score, boolean overall_status, Date open_date, List<ReportEntry> entries) {
		this.overall_score = overall_score;
		this.overall_status = overall_status;
		this.open_date = open_date;
		this.entries = entries;
	}
	
	public int getOverall_score() {
		return overall_score;
	}
	public void setOverall_score(int overall_score) {
		this.overall_score = overall_score;
	}
	public boolean isOverall_status() {
		return overall_status;
	}
	public void setOverall_status(boolean overall_status) {
		this.overall_status = overall_status;
	}
	public Date getOpen_date() {
		return open_date;
	}
	public void setOpen_date(Date open_date) {
		this.open_date = open_date;
	}
	public List<ReportEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<ReportEntry> entries) {
		this.entries = entries;
	}
    
    

}
