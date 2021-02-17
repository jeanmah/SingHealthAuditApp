package main_package;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

abstract class Report {
	private int report_id;
	private int overall_score;
	private boolean overall_status;
	private Date open_date;
	private List<Entry> entries;
	
}

class Open_Report extends Report{
	private Date last_update_date;
}

class Close_Report extends Report{
	private Date close_date;
}

//Entries are never modified or deleted, only added.
//Entries with the same qn_id essentially "overrule" older entries with the same qn_id.
abstract class Entry {
	private int entry_id;
	private int qn_id;
	private Date date;
	private Time time;
	private String remarks;
	private BufferedImage evidence;
}

class AuditorEntry extends Entry{
	private Component_Status status;
}

class TenantEntry extends Entry{
	private Service_Status status;
}

enum Service_Status{
	SERVICED(true),			//Auditor attended entry
	NEEDS_SERVICING(false);	//Auditor attention needed
	
	private final boolean status;
	Service_Status(boolean pass){
		status = pass;
	}
	public boolean isStatus() {
		return status;
	}
}

enum Component_Status{
	PASS(true),				//Component Passed
	FAIL(false);			//Component Failed, needs Tenant attention
	
	private final boolean status;
	Component_Status(boolean pass){
		status = pass;
	}
	public boolean isStatus() {
		return status;
	}
	
	
	
}