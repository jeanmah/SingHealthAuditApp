package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.util.List;

public class OpenReport extends Report{
    private Date last_update_date;

    public OpenReport(int overall_score, boolean overall_status, Date open_date, List<ReportEntry> entries,
			Date last_update_date) {
		super(overall_score, overall_status, open_date, entries);
		this.last_update_date = last_update_date;
	}
    
    public Date getLast_update_date() {
        return last_update_date;
    }

	public void setLast_update_date(Date last_update_date) {
        this.last_update_date = last_update_date;
    }
}
