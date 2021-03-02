package com.SHAudit.singHealthAudit.Admin.Manager;

import com.SHAudit.singHealthAudit.Admin.Auditor.Auditor;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, Integer> {

    @Query("SELECT acc_id FROM managers WHERE branch_id= :branch_id")
    int getManagerIdFromBranchId(@Param("branch_id") String branch_id);

}
