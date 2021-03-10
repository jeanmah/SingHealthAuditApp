package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.util.List;


public class ClosedReport extends Report{
    private Date close_date;

    public ClosedReport(int overall_score, boolean overall_status, Date open_date, List<ReportEntry> entries,
			Date close_date) {
		super(overall_score, overall_status, open_date, entries);
		this.close_date = close_date;
	}

	public Date getClose_date() {
        return close_date;
    }

    public void setClose_date(Date close_date) {
        this.close_date = close_date;
    }
}