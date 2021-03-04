package com.SHAudit.singHealthAudit.Audit;

import java.sql.Date;
import java.util.List;

public class Open_Report extends Report{
    private Date last_update_date;

//im very confused by all the parameters
    public Open_Report(int report_id, int overall_score, boolean overall_status, Date open_date, List<Entry> entries, Date last_update_date) {
        super(report_id, overall_score, overall_status, open_date, entries);
        this.last_update_date = last_update_date;
    }

    public Date getLast_update_date() {
        return last_update_date;
    }

    public void setLast_update_date(Date last_update_date) {
        this.last_update_date = last_update_date;
    }
}
