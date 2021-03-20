package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.Collection;

import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;

@Repository
public interface AuditorRepo extends CrudRepository<AuditorModel, Integer>{
	@Query("SELECT * FROM Auditors")
	Collection<AuditorModel> getAll();

	@Query("SELECT * FROM Auditors WHERE acc_id = :acc_id LIMIT 1")
	AuditorModel getAuditorById(@Param("acc_id") int acc_id);
	
	@Query("SELECT branch_id FROM Auditors WHERE acc_id= :acc_id")
	String getBranchIDfromAccountID(@Param("acc_id") int auditor_acc_id);
	
	@Query("SELECT mgr_id FROM Auditors WHERE acc_id= :acc_id")
	int getManagerIDfromAuditorID(@Param("acc_id") int auditor_acc_id);


}
