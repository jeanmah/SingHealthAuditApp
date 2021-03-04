package com.SHAudit.singHealthAudit.Admin.Auditor;

import java.util.List;

import com.SHAudit.singHealthAudit.Admin.Tenant.Tenant;
import com.SHAudit.singHealthAudit.Admin.Tenant.TenantRepository;
import com.SHAudit.singHealthAudit.Admin.mySQLAccount.Account;
import com.SHAudit.singHealthAudit.Admin.mySQLAccount.AccountRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class AuditorController {
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private AuditorRepository auditorRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //get all the tenants for a particular auditor
    @GetMapping("/a/alltenants")
    public ResponseEntity<?> getTenantForAuditor(@AuthenticationPrincipal UserDetails auditorUser) {

        Account auditorAccount = accountRepository.findByUsername(auditorUser.getUsername());
        int auditorId = auditorAccount.getAccount_id();
        logger.info("found auditor id {}", auditorId);
        String branchId = auditorRepository.getBranchIDfromAuditorID(auditorId);
        logger.info("found branch id {}", branchId);

        if(branchId==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if(tenantRepository == null)  logger.info("tenant repo null");

        List<Tenant> tenants = tenantRepository.getAllTenantsByBranchId(branchId);
        logger.info("found tenant id {}", tenants.get(0).getAcc_id());

        if(tenants==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        JSONArray outputJArray = new JSONArray();
        //get the accounts for the tenants
        for(Tenant t: tenants){
            JSONObject tenantInfo = new JSONObject();
            Account tenantAccount = accountRepository.findByAccId(t.getAcc_id());
            tenantInfo.put("acc_id",tenantAccount.getAccount_id());
            tenantInfo.put("employee_id",tenantAccount.getEmployee_id());
            tenantInfo.put("first_name",tenantAccount.getFirst_name());
            tenantInfo.put("last_name",tenantAccount.getLast_name());
            tenantInfo.put("store_addr", t.getStore_addr());
            tenantInfo.put("FB_NFB",t.getType_id());

            outputJArray.add(tenantInfo);
        }

        JSONObject mainOutput = new JSONObject();
        mainOutput.put("tenants", outputJArray);

        return ResponseEntity.ok(mainOutput);
    }

    //get all the tenant info
    @GetMapping("/a/tenant/{tenantId}")
    public ResponseEntity<?> geTenantInfo(@PathVariable("tenantId") int tenantId) {
        Tenant tenant = tenantRepository.getTenantById(tenantId);
        if(tenant==null) {
            logger.warn("tenant with id {} not found",tenantId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else{
            //get the accounts for the tenants
            JSONObject tenantInfo = new JSONObject();
            Account tenantAccount = accountRepository.findByAccId(tenant.getAcc_id());
            tenantInfo.put("acc_id",tenantAccount.getAccount_id());
            tenantInfo.put("employee_id",tenantAccount.getEmployee_id());
            tenantInfo.put("first_name",tenantAccount.getFirst_name());
            tenantInfo.put("last_name",tenantAccount.getLast_name());
            tenantInfo.put("FB_NFB",tenant.getType_id());

            JSONObject mainOutput = new JSONObject();
            mainOutput.put("tenant", tenantInfo);

            return ResponseEntity.ok(mainOutput);
        }

    }

}

