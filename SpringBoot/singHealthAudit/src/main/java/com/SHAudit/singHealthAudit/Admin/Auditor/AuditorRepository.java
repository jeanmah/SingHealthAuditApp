package com.SHAudit.singHealthAudit.Admin.Auditor;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditorRepository extends CrudRepository<Auditor, Integer> {
    @Query("SELECT * FROM Auditors")
    List<Auditor> findAll();

    @Query("SELECT branch_id FROM Auditors WHERE acc_id= :acc_id")
    String getBranchIDfromAuditorID(@Param("acc_id") int acc_id);

    @Query("SELECT mgr_id FROM Auditors WHERE acc_id= :acc_id")
    int getManagerIDfromAuditorID(@Param("acc_id") int acc_id);

}
