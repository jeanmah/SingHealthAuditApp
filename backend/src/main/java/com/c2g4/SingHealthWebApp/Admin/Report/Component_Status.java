package com.c2g4.SingHealthWebApp.Admin.Report;

/**
 * Enum for use in ReportEntry to track its status
 * @author LunarFox
 *
 */
public enum Component_Status {
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