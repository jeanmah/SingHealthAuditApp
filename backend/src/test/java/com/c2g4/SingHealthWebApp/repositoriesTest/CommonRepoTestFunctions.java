package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonRepoTestFunctions {


    public static void clearAllTables(AccountRepo accountRepo, ManagerRepo managerRepo, AuditorRepo auditorRepo,
                                      TenantRepo tenantRepo, CompletedAuditRepo completedAuditRepo, OpenAuditRepo openAuditRepo){
        completedAuditRepo.deleteAll();
        openAuditRepo.deleteAll();
        managerRepo.deleteAll();
        auditorRepo.deleteAll();
        tenantRepo.deleteAll();
        accountRepo.deleteAll();
    }

}
