package com.c2g4.SingHealthWebApp.Admin.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class AccountController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private AuditorRepo auditorRepo;
    @Autowired
    private ManagerRepo managerRepo;

    //- returns list of all users and their user_ids
    @GetMapping("/account/getAllUsers")
    public void getAllUsers(){

    }

    //returns list of all users and their user_ids of a specific type
    @GetMapping("/account/getAllUsersofType")
    public void getAllUsersofType(String type){

    }

    @GetMapping("/account/getAllUsersofBranch")
    public void getAllUsersofBranch(String branch){

    }

    //Returns account information of the specified_user at specified access_level
    @GetMapping("/account/getUserProfile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails callerUser, int user_id){
        //check who is calling
        AccountModel callerAccount = accountRepo.findByUsername(callerUser.getUsername());
        if (callerAccount ==null) return ResponseEntity.badRequest().body(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String accJsonString = getAccount(user_id);
        ObjectNode accountNode;
        try {
            accountNode = (ObjectNode) objectMapper.readTree(accJsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
        if(!checkValidAccessLevel(callerAccount,accountNode,user_id)){
            return ResponseEntity.badRequest().body(null);
        }
        String roleType = accountNode.get("role_id").asText();
        String specificTypeJsonString;
        switch (roleType) {
            case "tenant":
                specificTypeJsonString = getTenant(user_id);
                break;
            case "auditor":
                specificTypeJsonString = getAuditor(user_id);
                break;
            case "manager":
                specificTypeJsonString = getManager(user_id);
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }

        if(specificTypeJsonString == null){
            return ResponseEntity.badRequest().body(null);
        }

        ObjectNode typeNode;
        try {
            typeNode = (ObjectNode) objectMapper.readTree(specificTypeJsonString);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
        JsonNode merged = accountNode.setAll(typeNode);
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(merged));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/account/postProfileUpdate")
    public ResponseEntity<?> postProfileUpdate(
            @RequestPart(value = "changes", required = true) String changes){
				return null;

    }

    private boolean checkValidAccessLevel(AccountModel callerAccount,ObjectNode requestedAccountNode, int user_id){
        //tenants can only call themselves
        //auditors can call all tenants under the same branch
        //managers can call all

        //if calling yourself true
        if (user_id==callerAccount.getAccount_id()) return true;
        switch (callerAccount.getRole_id()) {
            case "auditor":
                if(requestedAccountNode.get("role_id").asText().equals("tenant")){
                    //check if tenant is under the same branch
                    return requestedAccountNode.get("branch_id").asText().equals(callerAccount.getBranch_id());
                } else if(requestedAccountNode.get("role_id").asText().equals("manager")) return false;
                break;
            case "manager":
                return true;
            default:
                return false;
        }
        return false;
    }

    //only accesses the Account Table
    private String getAccount(int user_id){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer().withoutAttribute("password");

        AccountModel accountModel = accountRepo.findByAccId(user_id);
        if(accountModel == null) return null;
        String accJsonString;
        try {
            return writer.writeValueAsString(accountModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    //only accesses the Tenant Table
    private String getTenant(int user_id){
        TenantModel tenantModel = tenantRepo.getTenantById(user_id);
        if(tenantModel == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer().withoutAttribute("acc_id").withoutAttribute("branch_id");
        try {
            return writer.writeValueAsString(tenantModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    //only accesses the Auditor Table
    private String getAuditor(int user_id){
        AuditorModel auditorModel = auditorRepo.getAuditorById(user_id);
        if(auditorModel == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer().withoutAttribute("acc_id").withoutAttribute("branch_id");
        try {
            return writer.writeValueAsString(auditorModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    //only accesses the Manager Table
    private String getManager(int user_id){
        ManagerModel managerModel = managerRepo.getManagerById(user_id);
        if(managerModel == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer().withoutAttribute("acc_id").withoutAttribute("branch_id");
        try {
            return writer.writeValueAsString(managerModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


}
