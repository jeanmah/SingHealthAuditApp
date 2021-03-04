package com.SHAudit.singHealthAudit.Audit;

import com.sun.istack.Nullable;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.Time;

//Entries are never modified or deleted, only added.
//Entries with the same qn_id essentially "overrule" older entries with the same qn_id.
public abstract class Entry {
    private int entry_id; //do we really need an entry id?
    private int qn_id;
    private Date date;
    private Time time;

    @Nullable
    private int severity; //for now this is 0-nothing, 1-low, 2-med, 3-high
    @Nullable
    private String remarks;
    @Nullable
    //private BufferedImage evidence;
    private String evidence;
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

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }
}
