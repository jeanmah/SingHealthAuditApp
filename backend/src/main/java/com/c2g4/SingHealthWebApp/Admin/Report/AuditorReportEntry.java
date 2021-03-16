package com.c2g4.SingHealthWebApp.Admin.Report;

public class AuditorReportEntry extends ReportEntry{
	
    private Component_Status status;

    public Component_Status getStatus() {
        return status;
    }

    public void setStatus(boolean statusBool) {
        status = statusBool? Component_Status.PASS : Component_Status.FAIL;
    }
}
