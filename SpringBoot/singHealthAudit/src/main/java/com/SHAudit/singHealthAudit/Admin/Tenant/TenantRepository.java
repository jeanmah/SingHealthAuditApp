package com.SHAudit.singHealthAudit.Admin.Tenant;

import com.SHAudit.singHealthAudit.Admin.Auditor.Auditor;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, Integer> {
    @Query("SELECT * FROM tenant WHERE branch_id = :branch_id")
    List<Tenant> getAllTenantsByBranchId(@Param("branch_id") String branch_id);

    @Query("SELECT * FROM tenant WHERE acc_id = :acc_id LIMIT 1")
    Tenant getTenantById(@Param("acc_id") int acc_id);
}
