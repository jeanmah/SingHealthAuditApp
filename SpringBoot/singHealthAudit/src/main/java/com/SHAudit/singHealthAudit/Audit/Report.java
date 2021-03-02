package com.SHAudit.singHealthAudit.Audit;

import java.sql.Date;
import java.util.List;

public abstract class Report {
    protected int report_id;
    protected int overall_score;
    protected boolean overall_status;
    protected Date open_date;
    protected List<Entry> entries;

    public Report(int report_id, int overall_score, boolean overall_status, Date open_date, List<Entry> entries) {
        this.report_id = report_id;
        this.overall_score = overall_score;
        this.overall_status = overall_status;
        this.open_date = open_date;
        this.entries = entries;
    }
}
