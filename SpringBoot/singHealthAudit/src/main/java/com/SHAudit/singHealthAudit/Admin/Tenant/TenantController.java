package com.SHAudit.singHealthAudit.Admin.Tenant;

import com.SHAudit.singHealthAudit.Admin.mySQLAccount.Account;
import com.SHAudit.singHealthAudit.Admin.mySQLAccount.AccountRepository;
import com.SHAudit.singHealthAudit.Audit.OpenAudits;
import com.SHAudit.singHealthAudit.Audit.OpenAuditsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class TenantController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OpenAuditsRepository openAuditsRepository;

    //TODO: make this for all audits
    @GetMapping("/t/view/pastaudits")
    public ResponseEntity<?> auditorHome(@AuthenticationPrincipal UserDetails tenantUser) {
        Account tenantAccount = accountRepository.findByUsername(tenantUser.getUsername());
        int tenantId = tenantAccount.getAccount_id();
        //String tenantAudits = tenantRepository.getPastAuditById(tenantId);
        int openAuditId = tenantRepository.getOpenAuditById(tenantId);
        OpenAudits openAudits = openAuditsRepository.getOpenAuditById(openAuditId);
        boolean hasAudit = true;

        Map<String, Object> response = new HashMap<>();

        if(openAudits==null){
            response.put("openAudits","no audits");

            return ResponseEntity.ok(response);
        }
        List<OpenAudits> openAuditsList = new ArrayList<>();
        openAuditsList.add(openAudits);
        response.put("openAudits",openAuditsList);
        return ResponseEntity.ok(response);
    }

}
