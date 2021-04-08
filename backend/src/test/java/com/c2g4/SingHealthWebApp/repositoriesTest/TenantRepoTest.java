package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class TenantRepoTest {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private AuditorRepo auditorRepo;
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private CompletedAuditRepo completedAuditRepo;
    @Autowired
    private OpenAuditRepo openAuditRepo;

    private static final int auditor_id = 1003;
    private static final int tenant_id = 1004;
    private static final int manager_id = 1001;

    @Test
    public void getAllAuditors() {
        List<AuditorModel> actualModels  = auditorRepo.getAllAuditors();
        assert(actualModels.size() >0);
    }

    @Test
    public void getAllAuditorsNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        List<AuditorModel> actualModels  = auditorRepo.getAllAuditors();
        assert(actualModels.size()==0);
    }

//
//    @Query("SELECT * FROM Tenant WHERE branch_id = :branch_id")
//    List<TenantModel> getAllTenantsByBranchId(@Param("branch_id") String branch_id);
//
//    @Query("SELECT * FROM Tenant WHERE acc_id = :acc_id LIMIT 1")
//    TenantModel getTenantById(@Param("acc_id") int acc_id);
//
//    @Query("SELECT past_audits FROM Tenant WHERE acc_id= :acc_id")
//    String getPastAuditById(@Param("acc_id") int acc_id);
//
//    @Query("SELECT latest_audit FROM Tenant WHERE acc_id= :acc_id")
//    int getOpenAuditById(@Param("acc_id") int acc_id);
//
//    @Query("SELECT past_audits FROM Tenant WHERE acc_id= :acc_id")
//    String getPastAuditsById(@Param("acc_id") int acc_id);
//
//    @Query("SELECT store_name FROM Tenant WHERE acc_id= :acc_id")
//    String getStoreNameById(@Param("acc_id") int acc_id);
//
//    @Modifying
//    @Query("UPDATE Tenant t SET t.past_audits = :past_audits WHERE t.acc_id = :acc_id")
//    void updatePastAuditsByTenantId(@Param("acc_id") int acc_id, @Param("past_audits") String past_audits);
//
//
//    @Modifying
//    @Query("UPDATE Tenant t SET t.latest_audit = :latest_audit WHERE t.acc_id = :acc_id")
//    void updateLatestAuditByTenantId(@Param("acc_id") int acc_id, @Param("latest_audit") int latest_audit);
//
//    @Modifying
//    @Query("UPDATE Tenant t SET t.latest_audit = -1 WHERE t.acc_id = :acc_id")
//    void removeLatestAuditByTenantId(@Param("acc_id") int acc_id);
//
//    @Modifying
//    @Query("UPDATE Tenant t SET t.audit_score = :audit_score WHERE t.acc_id = :acc_id")
//    void updateAuditScoreByTenantId(@Param("acc_id") int acc_id, @Param("audit_score") int audit_score);
//
//
//    @Query("SELECT * FROM Tenant ")
//    List<TenantModel> getAllTenants();
}
