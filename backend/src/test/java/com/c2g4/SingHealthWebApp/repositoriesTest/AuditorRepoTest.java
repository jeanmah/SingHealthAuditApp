package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
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
public class AuditorRepoTest {
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

    @BeforeEach
    public void clearRepo(){
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
    }

//    @Query("SELECT * FROM Auditors")
//    List<AuditorModel> getAllAuditors();
//
//    @Query("SELECT * FROM Auditors WHERE acc_id = :acc_id LIMIT 1")
//    AuditorModel getAuditorById(@Param("acc_id") int acc_id);
//
//    @Query("SELECT branch_id FROM Auditors WHERE acc_id= :acc_id")
//    String getBranchIDfromAccountID(@Param("acc_id") int auditor_acc_id);
//
//    @Query("SELECT mgr_id FROM Auditors WHERE acc_id= :acc_id")
//    int getManagerIDfromAuditorID(@Param("acc_id") int auditor_acc_id);
//
//    @Modifying
//    @Query("UPDATE Auditors a SET a.outstanding_audits = :outstanding_audits WHERE a.acc_id = :acc_id")
//    void updateLatestOutstandingAuditsByAuditorId(@Param("acc_id") int acc_id, @Param("outstanding_audits") String outstanding_audits);
//
//    @Query("SELECT outstanding_audits FROM Auditors WHERE acc_id= :acc_id")
//    String getOutstandingAuditsFromAuditorID(@Param("acc_id") int auditor_acc_id);
//
//    @Modifying
//    @Query("UPDATE Auditors a SET a.completed_audits = :completed_audits WHERE a.acc_id = :acc_id")
//    void updateLatestCompletedAuditsByAuditorId(@Param("acc_id") int acc_id, @Param("completed_audits") String completed_audits);
//
//    @Query("SELECT completed_audits FROM Auditors WHERE acc_id= :acc_id")
//    String getCompletedAuditsFromAuditorID(@Param("acc_id") int auditor_acc_id);
}
