package com.c2g4.SingHealthWebApp.Admin.Controllers;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class AccountController {
    @Autowired
    private AccountRepo accountRepo;

    //- returns list of all users and their user_ids
    @GetMapping("/account/getAllUsers")
    public ResponseEntity<?> getAllUsers(){

    }

    //returns list of all users and their user_ids of a specific type
    @GetMapping("/account/getAllUsersofType")
    public ResponseEntity<?> getAllUsersofType(String type){

    }

    @GetMapping("/account/getAllUsersofBranch")
    public ResponseEntity<?> getAllUsersofBranch(String branch){

    }


    public ResponseEntity<?> getUserProfile(String user_id, int access_level) - Returns account information of the specified_user at specified access_level
    postProfileUpdate(JSON changes)
    getAccount(int user_id) - only accesses the Account Table

    //only accesses the Tenant Table
    private Json getTenant(int user_id){

    }



    AccountModel auditorAccount = accountRepo.findByUsername(auditorUser.getUsername());
        if(auditorAccount==null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    int auditorId = auditorAccount.getAccount_id();
        logger.info("found auditor id {}", auditorId);
    String branchId = auditorAccount.getBranch_id();//auditorRepository.getBranchIDfromAuditorID(auditorId);
        logger.info("found branch id {}", branchId);
    //branch id could be null

    List<TenantModel> tenants = tenantRepo.getAllTenantsByBranchId(branchId);
        if(tenants==null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
        logger.info("found tenant id {}", tenants.get(0).getAcc_id());

    ObjectMapper objectmapper = new ObjectMapper();
    String tenantsAsJSONString = null;
		try {
        tenantsAsJSONString = objectmapper.writeValueAsString(tenants);
    } catch (
    JsonProcessingException e) {
        e.printStackTrace();
    }

        return ResponseEntity.ok(tenantsAsJSONString);
    getAuditor(int user_id) - only accesses the Auditor Table
    getManager(int user_id) - only accesses the Manager Table

    @GetMapping("/home/{accountType}")
    public ResponseEntity<?> auditorHome(@AuthenticationPrincipal UserDetails user, @PathVariable("accountType") String accountType) {
        AccountModel account = accountRepo.findByUsername(user.getUsername());
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
