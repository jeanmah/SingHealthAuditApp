package com.SHAudit.singHealthAudit.Audit;

public class AuditorEntry extends Entry{
    private Component_Status status;

    public boolean getStatus() {
        return status.isStatus();
    }

    public void setStatus(boolean statusBool) {
        status = statusBool? Component_Status.PASS : Component_Status.FAIL;
    }


}