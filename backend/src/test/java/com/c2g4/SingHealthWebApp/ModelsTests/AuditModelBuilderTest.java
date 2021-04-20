package com.c2g4.SingHealthWebApp.ModelsTests;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuditModelBuilderTest {
    public static AuditModelBuilder auditModelBuilder;

    @BeforeEach
    public void beforeFunc(){
        auditModelBuilder = new AuditModelBuilder();
    }

    @Test
    public void setUserIDs(){
        auditModelBuilder.setUserIDs(1,2,3);
        assert (auditModelBuilder.getTenantId()==1);
        assert (auditModelBuilder.getAuditorId()==2);
        assert (auditModelBuilder.getManagerId()==3);
    }

    @Test
    public void setNeed(){
        auditModelBuilder.setNeed(1,1,0);
        assert (auditModelBuilder.getNeedTenant()==1);
        assert (auditModelBuilder.getNeedAuditor()==1);
        assert (auditModelBuilder.getNeedManager()==0);
    }

}
