package com.SHAudit.singHealthAudit.Audit;

public enum Service_Status {
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
