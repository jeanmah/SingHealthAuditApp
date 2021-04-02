package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * ReportEntry is a representation of an entry of Report used for manipulation.
 * Entries are never modified or deleted, only added and entries with the same qn_id essentially "overrule" older entries with the same qn_id.
 * @author LunarFox
 *
 */

@Component
public class ReportEntry {
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
	  
	  private Component_Status status;
	  
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
		  this.images = images;
	  }
	
	  public void addImage(String base64img) {
		  if(this.images == null) {
			  this.images = new ArrayList<String>();
		  }
		  this.images.add(base64img);
	  }
	
	  public int getSeverity() {
	      return severity;
	  }
	
	  public void setSeverity(int severity) {
	      this.severity = severity;
	  }
	  
	  public Component_Status getStatus() {
	      return status;
	  }
	  @JsonSetter
	  public void setStatus(String statusStr) {
		  switch (statusStr) {
			  case "PASS" -> status = Component_Status.PASS;
			  case "FAIL" -> status = Component_Status.FAIL;
			  default -> status = Component_Status.NA;
		  }
	  }
	
	  public void setStatus(int statusBool) {
		  switch (statusBool) {
			  case 1 -> status = Component_Status.PASS;
			  case 0 -> status = Component_Status.FAIL;
			  default -> status = Component_Status.NA;
		  }
	  }

	  public Date getDueDate(){
	  	int days;
	  	switch (severity){
			case 0:
				return null;
			case 1:
				days = 7;
				break;
			case 2:
				days = 3;
				break;
			case 3:
				days = 1;
				break;
			default:
				days = 0;
				break;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return new Date(c.getTimeInMillis());
	  }
	  

}



