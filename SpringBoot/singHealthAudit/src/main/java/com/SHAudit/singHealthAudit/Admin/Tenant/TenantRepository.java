package com.SHAudit.singHealthAudit.Admin.Tenant;

import com.SHAudit.singHealthAudit.Admin.Auditor.Auditor;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, Integer> {
    @Query("SELECT * FROM Tenant WHERE branch_id = :branch_id")
    List<Tenant> getAllTenantsByBranchId(@Param("branch_id") String branch_id);

    @Query("SELECT * FROM Tenant WHERE acc_id = :acc_id LIMIT 1")
    Tenant getTenantById(@Param("acc_id") int acc_id);

    @Query("SELECT past_audits FROM Tenant WHERE acc_id= :acc_id")
    String getPastAuditById(@Param("acc_id") int acc_id);

    @Query("SELECT latest_audit FROM Tenant WHERE acc_id= :acc_id")
    int getOpenAuditById(@Param("acc_id") int acc_id);

    @Modifying
    @Query("UPDATE Tenant t SET t.latest_audit = :latest_audit WHERE t.acc_id = :acc_id")
    void updateLatestAuditByTenantId(@Param("acc_id") int acc_id, @Param("latest_audit") int latest_audit);
}
