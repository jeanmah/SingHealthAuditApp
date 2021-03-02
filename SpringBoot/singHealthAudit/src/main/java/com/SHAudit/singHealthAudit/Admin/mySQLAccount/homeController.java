package com.SHAudit.singHealthAudit.Admin.mySQLAccount;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class homeController {

    @GetMapping("/home/a")
    public String auditorHome(@AuthenticationPrincipal Account auditorAccount) {
        return ""; //for now home does nothing
    }

    @GetMapping("/home/t")
    public String tenantHome(@AuthenticationPrincipal Account tenantAccount) {
        return ""; //for now home does nothing
    }

    @GetMapping("/home/m")
    public String managerHome(@AuthenticationPrincipal Account managerAccount) {
        return ""; //for now home does nothing
    }

}
