package com.c2g4.SingHealthWebApp.Admin.Repositories;

public interface AuditCheckListRepo{
	
    String getCategoryByQnID(int fb_qn_id);
    
    double getWeightByQnID(int category);

}
