package com.SHAudit.singHealthAudit.Audit;

import java.sql.Date;
import java.util.List;

public class Close_Report extends Report{
    private Date close_date;

    public Close_Report(int report_id, int overall_score, boolean overall_status, Date open_date, List<Entry> entries, Date close_date) {
        super(report_id, overall_score, overall_status, open_date, entries);
        this.close_date = close_date;
    }

    public Date getClose_date() {
        return close_date;
    }

    public void setClose_date(Date close_date) {
        this.close_date = close_date;
    }
}
