package com.SHAudit.singHealthAudit.Admin.mySQLAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class homeController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/home/{accountType}")
    public ResponseEntity<?> auditorHome(@AuthenticationPrincipal UserDetails user, @PathVariable("accountType") String accountType) {
            Account account = accountRepository.findByUsername(user.getUsername());
            if(accountType.equals("a")) {
            if(!account.getRole_id().equals("Auditor")) return ResponseEntity.badRequest().body("user is not an Auditor");
            return ResponseEntity.ok(account.getFirst_name());
        }
        else if (accountType.equals("t")) {
            if(!account.getRole_id().equals("Tenant")) return ResponseEntity.badRequest().body("user is not an Tenant");
                return ResponseEntity.ok(account.getFirst_name());
            }
        else if (accountType.equals("m")){
            if(!account.getRole_id().equals("Manager")) return ResponseEntity.badRequest().body("user is not an Manager");
                return ResponseEntity.ok(account.getFirst_name());
            }
        else return ResponseEntity.badRequest().body("home page does not exist");
    }

}
