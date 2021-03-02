package com.SHAudit.singHealthAudit.Audit;

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