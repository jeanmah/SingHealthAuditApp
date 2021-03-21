package com.c2g4.SingHealthWebApp.Admin.Controllers;

import com.c2g4.SingHealthWebApp.Admin.Models.*;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * only authorized for a manager, returns a list of all users.
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @return a JsonArray of users with keys {acc_id, first_name, last_name},
     * if the callerUser is not a manager, returns HttpStatus UNAUTHORIZED with body "Unauthorized",
     * if any other errors occur along the way, return http BAD_REQUEST
     */
    @GetMapping("/account/getAllUsers")
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal UserDetails callerUser){
        AccountModel callerAccount = accountRepo.findByUsername(callerUser.getUsername());
        if (callerAccount ==null) return ResponseEntity.badRequest().body(null);
        if(!callerAccount.getRole_id().equals("Manager")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        List<AccountModel> allAccounts = accountRepo.getAllAccounts();
        if(allAccounts == null) return ResponseEntity.badRequest().body(null);

        ArrayNode output = getBasicAccFieldsArray(allAccounts);
        return ResponseEntity.ok(output);
    }

    /**
     * only authorized for a manager, returns a list of all users from a branch
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param branch_id a String of the branch to query from
     * @return a JsonArray of users with keys {acc_id, first_name, last_name},
     * if the callerUser is not a manager, returns HttpStatus UNAUTHORIZED with body "Unauthorized",
     * if any other errors occur along the way, return http BAD_REQUEST
     */
    @GetMapping("/account/getAllUsersofBranch")
    public ResponseEntity<?> getAllUsersofBranch(@AuthenticationPrincipal UserDetails callerUser, @RequestParam String branch_id){
        AccountModel callerAccount = accountRepo.findByUsername(callerUser.getUsername());
        if (callerAccount ==null) return ResponseEntity.badRequest().body(null);
        if(!callerAccount.getRole_id().equals("Manager")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        List<AccountModel> allAccountsByBranchId = accountRepo.getAllAccountsByBranchId(branch_id);
        if(allAccountsByBranchId == null) return ResponseEntity.badRequest().body(null);
        ArrayNode output = getBasicAccFieldsArray(allAccountsByBranchId);
        return ResponseEntity.ok(output);
    }

    /**
     * used to create an arrayNode of basic Account attributes
     * @param accountModels a list of AccountModels
     * @return an ArrayNode basic account attributes of the accountModels
     */
    private ArrayNode getBasicAccFieldsArray(List<AccountModel> accountModels){
        ArrayNode output = objectMapper.createArrayNode();
        for(AccountModel accountModel:accountModels){
            ObjectNode account = objectMapper.createObjectNode();
            account.put("acc_id", accountModel.getAccount_id());
            account.put("first_name", accountModel.getFirst_name());
            account.put("last_name", accountModel.getLast_name());
            output.add(account);
        }
        return output;
    }


    /**
     * gets an in depth list of user details of a certain type if the callerUser is authorized,
     * a tenant cannot use this function, auditors can only get tenants from their branch,
     * managers are fully authorized
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param roleType a String either Tenant, Auditor or Manager
     * @return a JsonArray of users with keys {account_id, employee_id, username,first_name,last_name,email,hp,role_id,branch_id},
     * if roleType == "Tenant" additional keys of {type_id,audit_score,latest_audit,past_audits,store_addr}
     * if roleType == "Auditor" additional keys of {completed_audits, appealed_audits, outstanding_audit_ids,mgr_id}
     * if roleType == "Manager" no additional keys
     * if the callerUser is unauthorized, returns HttpStatus UNAUTHORIZED with body "Unauthorized",
     * if any other errors occur along the way, return http BAD_REQUEST
     */
    @GetMapping("/account/getAllUsersofType")
    public ResponseEntity<?> getAllUsersofType(@AuthenticationPrincipal UserDetails callerUser,@RequestParam String roleType){
        AccountModel callerAccount = accountRepo.findByUsername(callerUser.getUsername());
        if (callerAccount ==null) return ResponseEntity.badRequest().body(null);
        if(callerAccount.getRole_id().equals("Tenant")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        } else if(callerAccount.getRole_id().equals("Auditor")){
            //Auditors can only get tenants from the same branch
            if(!roleType.equals("Tenant")) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            String branchId = callerAccount.getBranch_id();
            return getTenantsFromBranch(branchId);
        } else if(callerAccount.getRole_id().equals("Manager")){
            List<typeAccountModel> typeAccountModels;
            switch (roleType) {
                case "Tenant":
                    typeAccountModels = new ArrayList<typeAccountModel>(tenantRepo.getAllTenants());
                    break;
                case "Auditor":
                    typeAccountModels = new ArrayList<typeAccountModel>(auditorRepo.getAllAuditors());
                    break;
                case "Manager":
                    typeAccountModels = new ArrayList<typeAccountModel>(managerRepo.getAllManagers());
                    break;
                default:
                    return ResponseEntity.badRequest().body(null);
            }
            return userArrayJson(typeAccountModels);
        } else{
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * gets all the tenants from a particular Branch
     * @param branch_id a String of the branch to query from
     * @return a JsonArray of Tenants with keys {account_id, employee_id, username,first_name,last_name,email,hp,
     * role_id,branch_id,type_id,audit_score,latest_audit,past_audits,store_addr}
     */
    private ResponseEntity<?> getTenantsFromBranch(String branch_id){
        List<TenantModel> tenantModels = tenantRepo.getAllTenantsByBranchId(branch_id);
        if(tenantModels == null) return null;
        return userArrayJson(new ArrayList<typeAccountModel>(tenantModels));
    }

    /**
     * takes a list of models and finds the corresponding account entry, merges the model
     * and account entry into a JsonNode and returns an array of such JsonNodes
     * @param models a list of either TenantModel, AuditorModel or ManagerModel
     * @return a JsonArray of users with keys {account_id, employee_id, username,first_name,last_name,email,hp,role_id,branch_id},
     * if roleType == "Tenant" additional keys of {type_id,audit_score,latest_audit,past_audits,store_addr}
     * if roleType == "Auditor" additional keys of {completed_audits, appealed_audits, outstanding_audit_ids,mgr_id}
     * if roleType == "Manager" no additional keys
     * if any other errors occur along the way, return http BAD_REQUEST
     */
    private ResponseEntity<?> userArrayJson(List<typeAccountModel> models){
        ArrayNode output = objectMapper.createArrayNode();
        for(typeAccountModel model:models){
            String accJsonString = getAccount(model.getAcc_id());
            if(accJsonString == null) return ResponseEntity.badRequest().body(null);
            ObjectNode accountNode,typeNode;
            try {
                accountNode = (ObjectNode) objectMapper.readTree(accJsonString);
                typeNode = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(model));
                typeNode.remove("acc_id");

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(null);
            }
            output.add(accountNode.setAll(typeNode));
        }
        return ResponseEntity.ok(output);
    }

    /**
     * gets in depth user details of a certain user if the callerUser is authorized,
     * all users can call themselves,
     * a tenant cannot use this function, auditors can only get tenants from their branch,
     * managers are fully authorized
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param user_id an Optional int of the userId to query, must be present if firstName and lastName is not present
     * @param firstName an Optional String of the first name to query, must be present is user_id is not present
     * @param lastName an Optional String of the last name to query, must be present is user_id is not present
     * @return a JsonNode of requested user with keys {account_id, employee_id, username,first_name,last_name,email,hp,role_id,branch_id},
     * if roleType == "Tenant" additional keys of {type_id,audit_score,latest_audit,past_audits,store_addr}
     * if roleType == "Auditor" additional keys of {completed_audits, appealed_audits, outstanding_audit_ids,mgr_id}
     * if roleType == "Manager" no additional keys
     * if the callerUser is unauthorized, returns HttpStatus UNAUTHORIZED with body "Unauthorized",
     * if any other errors occur along the way, return http BAD_REQUEST
     */
    @GetMapping("/account/getUserProfile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails callerUser,
                                            @RequestParam(required = false) Optional<Integer> user_id,
                                            @RequestParam(required = false) Optional<String> firstName,
                                            @RequestParam(required = false) Optional<String> lastName
    ){
        if(user_id.isEmpty() && firstName.isEmpty() && lastName.isEmpty())
            return ResponseEntity.badRequest().body(null);

        //check who is calling
        AccountModel callerAccount = accountRepo.findByUsername(callerUser.getUsername());
        if (callerAccount ==null) return ResponseEntity.badRequest().body(null);
        int userID;
        if(user_id.isPresent()){
            userID = user_id.get();
        } else{
            if(firstName.isEmpty() || lastName.isEmpty())
                return ResponseEntity.badRequest().body(null);
            userID = accountRepo.getAccIdFromNames(firstName.get(),lastName.get());
        }
        String accJsonString = getAccount(userID);
        if(accJsonString == null) return ResponseEntity.badRequest().body(null);
        ObjectNode accountNode;
        try {
            accountNode = (ObjectNode) objectMapper.readTree(accJsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
        if(!checkValidAccessLevel(callerAccount,accountNode,userID)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String roleType = accountNode.get("role_id").asText();
        String specificTypeJsonString;
        switch (roleType) {
            case "Tenant":
                specificTypeJsonString = getTenant(userID);
                break;
            case "Auditor":
                specificTypeJsonString = getAuditor(userID);
                break;
            case "Manager":
                specificTypeJsonString = getManager(userID);
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
            typeNode.remove("acc_id");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
        JsonNode merged = accountNode.setAll(typeNode);

        return ResponseEntity.ok(merged);

    }

    /**
     * updates the profile of the callerUser, does not change the password
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param changes a String which is a Json of keys {username, first_name, last_name, email, hp}
     * @return http ok if ok and bad request if bad request
     */
    @PostMapping("/account/postProfileUpdate")
    public ResponseEntity<?> postProfileUpdate(@AuthenticationPrincipal UserDetails callerUser,
                                               @RequestPart(value = "changes") String changes){
        AccountModel callerAccount = accountRepo.findByUsername(callerUser.getUsername());
        if (callerAccount ==null) return ResponseEntity.badRequest().body(null);
        try {

            ObjectNode changesNode = objectMapper.readValue(changes, ObjectNode.class);
            if(changesNode.has("username") && changesNode.has("first_name") &&
                    changesNode.has("last_name") &&changesNode.has("email") &&
                    changesNode.has("hp")) {
                accountRepo.changeAccountFields(callerAccount.getAccount_id(),
                        changesNode.get("username").asText(), changesNode.get("first_name").asText(),
                        changesNode.get("last_name").asText(), changesNode.get("email").asText(),
                        changesNode.get("hp").asText());
                return ResponseEntity.ok().body(null);
            } else{
                return ResponseEntity.badRequest().body(null);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * encryptes the new password and updates the database
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal.
     * @param new_password String of the new password
     * @return http ok if ok and bad request if bad request
     */
    @PostMapping("/account/postPasswordUpdate")
    public ResponseEntity<?> postPasswordUpdate(@AuthenticationPrincipal UserDetails callerUser, @RequestPart(value = "new_password") String new_password){
        AccountModel callerAccount = accountRepo.findByUsername(callerUser.getUsername());
        if (callerAccount ==null) return ResponseEntity.badRequest().body(null);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(new_password);
        accountRepo.changePasswordByAccId(callerAccount.getAccount_id(),encodedPassword);
        return ResponseEntity.ok().body(null);
    }

    /**
     * checks if someone has access to get the info of other users
     * @param callerAccount the UserDetails of the caller taken from the Authentication Principal.
     * @param requestedAccountNode an ObjectNode of the account of the object to be requested
     * @param user_id an int of the user_id of the account to be requested
     * @return true if callerAccount is the account to be requested,
     * true if auditors calling tenants under the same branch,
     * true if the callerAccount is a manager,
     * false otherwise
     */
    private boolean checkValidAccessLevel(AccountModel callerAccount,ObjectNode requestedAccountNode, int user_id){
        if (user_id==callerAccount.getAccount_id()) return true;
        switch (callerAccount.getRole_id()) {
            case "Auditor":
                if(requestedAccountNode.get("role_id").asText().equals("Tenant")){
                    //check if tenant is under the same branch
                    return requestedAccountNode.get("branch_id").asText().equals(callerAccount.getBranch_id());
                } else if(requestedAccountNode.get("role_id").asText().equals("Manager")) return false;
                break;
            case "Manager":
                return true;
            default:
                return false;
        }
        return false;
    }

    /**
     * get an AccountModel from the user_id  as a String
     * @param user_id an int of the user_id to be queried
     * @return a String of the AccountModel with the user_id, null if anything goes wrong
     */
    private String getAccount(int user_id){
        AccountModel accountModel = accountRepo.findByAccId(user_id);
        if(accountModel == null) return null;
        try {
            return objectMapper.writeValueAsString(accountModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * get an TenantModel from the user_id  as a String
     * @param user_id an int of the user_id to be queried
     * @return a String of the TenantModel with the user_id, null if anything goes wrong
     */
    private String getTenant(int user_id){
        TenantModel tenantModel = tenantRepo.getTenantById(user_id);
        if(tenantModel == null) return null;
        try {
            return objectMapper.writeValueAsString(tenantModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * get an AuditorModel from the user_id  as a String
     * @param user_id an int of the user_id to be queried
     * @return a String of the AuditorModel with the user_id, null if anything goes wrong
     */
    private String getAuditor(int user_id){
        AuditorModel auditorModel = auditorRepo.getAuditorById(user_id);
        if(auditorModel == null) return null;
        try {
            return objectMapper.writeValueAsString(auditorModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * get an ManagerModel from the user_id  as a String
     * @param user_id an int of the user_id to be queried
     * @return a String of the ManagerModel with the user_id, null if anything goes wrong
     */
    private String getManager(int user_id){
        ManagerModel managerModel = managerRepo.getManagerById(user_id);
        if(managerModel == null) return null;
        try {
            return objectMapper.writeValueAsString(managerModel);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


}