package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.lang.Nullable;

//Entries are never modified or deleted, only added.
//Entries with the same qn_id essentially "overrule" older entries with the same qn_id.
public abstract class ReportEntry {
	  private int entry_id;
	  private int qn_id;
	  private Date date;
	  private Time time;
	
	  @Nullable
	  private int severity; //for now this is 0-nothing, 1-low, 2-med, 3-high
	  @Nullable
	  private String remarks;
	  @Nullable
	  private List<String> images;
	  
	  public int getEntry_id() {
	      return entry_id;
	  }
	
	  public void setEntry_id(int entry_id) {
	      this.entry_id = entry_id;
	  }
	
	  public int getQn_id() {
	      return qn_id;
	  }
	
	  public void setQn_id(int qn_id) {
	      this.qn_id = qn_id;
	  }
	
	  public Date getDate() {
	      return date;
	  }
	
	  public void setDate(Date date) {
	      this.date = date;
	  }
	
	  public Time getTime() {
	      return time;
	  }
	
	  public void setTime(Time time) {
	      this.time = time;
	  }
	
	  public String getRemarks() {
	      return remarks;
	  }
	
	  public void setRemarks(String remarks) {
	      this.remarks = remarks;
	  }
	  
	  public List<String> getImages(){
		  return images;
	  }
	  
	  public void setImages(List<String> images) {
		  //todo
		  this.images = images;
	  }
	
	  public void addImage(String base64img) {
		  this.images.add(base64img);
	  }
	
	  public int getSeverity() {
	      return severity;
	  }
	
	  public void setSeverity(int severity) {
	      this.severity = severity;
	  }
}