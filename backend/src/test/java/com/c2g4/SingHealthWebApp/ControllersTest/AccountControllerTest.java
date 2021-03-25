package com.c2g4.SingHealthWebApp.ControllersTest;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AccountRepo accountRepo;
    @MockBean
    private TenantRepo tenantRepo;
    @MockBean
    private AuditorRepo auditorRepo;
    @MockBean
    private ManagerRepo managerRepo;

    private static final String MANAGERUSENAME = "managerUsername";
    private static final String KNOWN_USER_PASSWORD = "test123";
    private static final String AUDITORUSENAME = "auditorUsername";
    private static final String TENANTUSENAME = "tenantUsername";

    private static final String MANAGER = "Manager";
    private static final String AUDITOR = "Auditor";
    private static final String TENANT = "Tenant";

    private static Map<String,sampleAccounts> sampleAccountHashMap = Map.ofEntries(
            Map.entry(MANAGER,new sampleAccounts(0,MANAGERUSENAME,"Marcus","Ho","HQ")),
            Map.entry(AUDITOR,new sampleAccounts(1,AUDITORUSENAME,"Hannah","Mah","branch_A")),
            Map.entry(TENANT,new sampleAccounts(1,AUDITORUSENAME,"Gregory","Mah","branch_A"))
    );

    @Before
    public void before() {
        AccountModel managerAccount = createAccount(MANAGER,"Marcus","Ho","HQ");
        AccountModel auditorAccount = createAccount(AUDITOR,"Hannah","Mah","branch_A");
        AccountModel tenantAccount = createAccount(TENANT,"Gregory","Mah","branch_A");
        given(accountRepo.findByUsername(MANAGERUSENAME)).willReturn(managerAccount);
        given(accountRepo.findByUsername(AUDITORUSENAME)).willReturn(auditorAccount);
        given(accountRepo.findByUsername(TENANTUSENAME)).willReturn(tenantAccount);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersAsManagerWithResults()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createAribraryUsers(allAccounts);
        given(accountRepo.getAllAccounts()).willReturn(allAccounts);
        mvc.perform(get("/account/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[?(@.acc_id == 0 && @.first_name == \"Marcus\" && @.last_name == \"Ho\" && @.role_id == \""+MANAGER+"\")]").exists())
                .andExpect(jsonPath("$.[?(@.acc_id == 0 && @.first_name == \"Hannah\" && @.last_name == \"Mah\" && @.role_id == \""+AUDITOR+"\")]").exists())
                .andExpect(jsonPath("$.[?(@.acc_id == 0 && @.first_name == \"Gregory\" && @.last_name == \"Mah\" && @.role_id == \""+TENANT+"\")]").exists());
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersAsManagerNoResults()
            throws Exception {
        given(accountRepo.getAllAccounts()).willReturn(null);
        mvc.perform(get("/account/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersAsAuditor()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createAribraryUsers(allAccounts);
        given(accountRepo.getAllAccounts()).willReturn(allAccounts);
        mvc.perform(get("/account/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllUsersAsTenant()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createAribraryUsers(allAccounts);
        given(accountRepo.getAllAccounts()).willReturn(allAccounts);
        mvc.perform(get("/account/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofBranchAsManagerWithResults()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createArbitraySameBranchUsers(allAccounts, "A");
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(allAccounts);
        mvc.perform(get("/account/getAllUsersofBranch")
                .param("branch_id", "A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[?(@.acc_id == 0 && @.first_name == \"Marcus\" && @.last_name == \"Ho\" && @.role_id == \""+MANAGER+"\")]").exists())
                .andExpect(jsonPath("$.[?(@.acc_id == 0 && @.first_name == \"Hannah\" && @.last_name == \"Mah\" && @.role_id == \""+AUDITOR+"\")]").exists())
                .andExpect(jsonPath("$.[?(@.acc_id == 0 && @.first_name == \"Gregory\" && @.last_name == \"Mah\" && @.role_id == \""+TENANT+"\")]").exists());
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofBranchAsManagerNoResults()
            throws Exception {
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(null);
        mvc.perform(get("/account/getAllUsersofBranch")
                .param("branch_id", "A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofBranchAsAuditor()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createArbitraySameBranchUsers(allAccounts, "A");
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(allAccounts);
        mvc.perform(get("/account/getAllUsersofBranch")
                .param("branch_id", "A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllUsersofBranchAsTenant()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createArbitraySameBranchUsers(allAccounts, "A");
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(allAccounts);
        mvc.perform(get("/account/getAllUsersofBranch")
                .param("branch_id", "A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeTenantAsManagerWithResults()
            throws Exception {
        ArrayList<TenantModel> tenantModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryTenants(tenantModels, accountModels);
        given(tenantRepo.getAllTenants()).willReturn(tenantModels);
        given(accountRepo.findByAccId(0)).willReturn(accountModels.get(0));
        given(accountRepo.findByAccId(1)).willReturn(accountModels.get(1));
        given(accountRepo.findByAccId(2)).willReturn(accountModels.get(2));

        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json("[{\"employee_id\":123,\"username\":\"Johndoh\",\"first_name\":\"John\"," +
                        "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\"," +
                        "\"acc_id\":0,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}," +
                        "{\"employee_id\":123,\"username\":\"Marydoh\",\"first_name\":\"Mary\",\"last_name\":\"doh\",\"email\":\"something@email.com\"," +
                        "\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":1,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0," +
                        "\"past_audits\":null,\"store_addr\":\"#01-02\"},{\"employee_id\":123,\"username\":\"Pauldoh\",\"first_name\":\"Paul\",\"last_name\":\"doh\"," +
                        "\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":2," +
                        "\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}]"));

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeTenantAsManagerNoResults()
            throws Exception {
        given(tenantRepo.getAllTenants()).willReturn(null);
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(null);
        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeAuditorAsManagerWithResults()
            throws Exception {
        ArrayList<AuditorModel> auditorModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryAuditors(auditorModels, accountModels);
        given(auditorRepo.getAllAuditors()).willReturn(auditorModels);
        given(accountRepo.findByAccId(0)).willReturn(accountModels.get(0));
        given(accountRepo.findByAccId(1)).willReturn(accountModels.get(1));
        given(accountRepo.findByAccId(2)).willReturn(accountModels.get(2));

        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json("[{\"employee_id\":123,\"username\":\"Johndoh\",\"first_name\":\"John\"," +
                        "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\"," +
                        "\"acc_id\":0,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}," +
                        "{\"employee_id\":123,\"username\":\"Marydoh\",\"first_name\":\"Mary\",\"last_name\":\"doh\",\"email\":\"something@email.com\"," +
                        "\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":1,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0," +
                        "\"past_audits\":null,\"store_addr\":\"#01-02\"},{\"employee_id\":123,\"username\":\"Pauldoh\",\"first_name\":\"Paul\",\"last_name\":\"doh\"," +
                        "\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":2," +
                        "\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}]"));

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeAuditorAsManagerNoResults()
            throws Exception {
        given(auditorRepo.getAllAuditors()).willReturn(null);
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(null);
        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeManagerAsManagerWithResults()
            throws Exception {
        ArrayList<ManagerModel> managerModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryManagers(managerModels, accountModels);
        given(managerRepo.getAllManagers()).willReturn(managerModels);
        given(accountRepo.findByAccId(0)).willReturn(accountModels.get(0));
        given(accountRepo.findByAccId(1)).willReturn(accountModels.get(1));
        given(accountRepo.findByAccId(2)).willReturn(accountModels.get(2));

        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json("[{\"employee_id\":123,\"username\":\"Johndoh\",\"first_name\":\"John\"," +
                        "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\"," +
                        "\"acc_id\":0,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}," +
                        "{\"employee_id\":123,\"username\":\"Marydoh\",\"first_name\":\"Mary\",\"last_name\":\"doh\",\"email\":\"something@email.com\"," +
                        "\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":1,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0," +
                        "\"past_audits\":null,\"store_addr\":\"#01-02\"},{\"employee_id\":123,\"username\":\"Pauldoh\",\"first_name\":\"Paul\",\"last_name\":\"doh\"," +
                        "\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":2," +
                        "\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}]"));

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeManagerAsManagerNoResults()
            throws Exception {
        given(managerRepo.getAllManagers()).willReturn(null);
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(null);
        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofTypeTenantAsAuditor()
            throws Exception {
        ArrayList<TenantModel> tenantModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryTenants(tenantModels, accountModels);
        System.out.println("number of tenants" + tenantModels.size());
        given(tenantRepo.getAllTenantsByBranchId("branch_A")).willReturn(tenantModels);
        given(accountRepo.findByAccId(0)).willReturn(accountModels.get(0));
        given(accountRepo.findByAccId(1)).willReturn(accountModels.get(1));
        given(accountRepo.findByAccId(2)).willReturn(accountModels.get(2));

        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json("[{\"employee_id\":123,\"username\":\"Johndoh\",\"first_name\":\"John\"," +
                        "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\"," +
                        "\"acc_id\":0,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}," +
                        "{\"employee_id\":123,\"username\":\"Marydoh\",\"first_name\":\"Mary\",\"last_name\":\"doh\",\"email\":\"something@email.com\"," +
                        "\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":1,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0," +
                        "\"past_audits\":null,\"store_addr\":\"#01-02\"},{\"employee_id\":123,\"username\":\"Pauldoh\",\"first_name\":\"Paul\",\"last_name\":\"doh\"," +
                        "\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"A\",\"acc_id\":2," +
                        "\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}]"));
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofTypeTenantAsAuditorNoResults()
            throws Exception {
        given(tenantRepo.getAllTenantsByBranchId("branch_A")).willReturn(null);
        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofTypeNotTenantAsAuditorNoResults()
            throws Exception {
        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", AUDITOR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllUsersofTypeAsTenant()
            throws Exception {
        mvc.perform(get("/account/getAllUsersofType")
                .param("roleType", TENANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    //TODO: getUserProfile
    //manager - manager, auditor, tenant, self
    //auditor - not tenant, tenant same branch, tenant not same branch self
    //tenant - not self, self

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileAsManagerUserIDWithResults()
            throws Exception {
        mvc.perform(get("/account/getUserProfile")
                .param("roleType", AUDITOR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileAsManagerUserIDNoResults()
            throws Exception {
        mvc.perform(get("/account/getUserProfile")
                .param("roleType", AUDITOR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    //TODO: postPasswordUpdate
    //TODO: postProfileUpdate


    private void createArbitraryTenants(ArrayList<TenantModel> tenantModels, ArrayList<AccountModel> accountModels){
        createTenant("John","doh",tenantModels,accountModels);
        createTenant("Mary","doh",tenantModels,accountModels);
        createTenant("Paul","doh",tenantModels,accountModels);
    }

    private void createArbitraryAuditors(ArrayList<AuditorModel> auditorModels, ArrayList<AccountModel> accountModels){
        createAuditor("John","doh",auditorModels,accountModels);
        createAuditor("Mary","doh",auditorModels,accountModels);
        createAuditor("Paul","doh",auditorModels,accountModels);
    }

    private void createArbitraryManagers(ArrayList<ManagerModel> managerModels, ArrayList<AccountModel> accountModels){
        createManager("John","doh",managerModels,accountModels);
        createManager("Mary","doh",managerModels,accountModels);
        createManager("Paul","doh",managerModels,accountModels);
    }


    private void createAribraryUsers(ArrayList<AccountModel> accountModels){
        accountModels.add(createAccount(MANAGER,"Marcus","Ho"));
        accountModels.add(createAccount(AUDITOR, "Hannah", "Mah"));
        accountModels.add(createAccount(TENANT, "Gregory","Mah"));
    }

    private void createArbitraySameBranchUsers(ArrayList<AccountModel> accountModels, String branch_id){
        accountModels.add(createAccount(MANAGER,"Marcus","Ho",branch_id));
        accountModels.add(createAccount(AUDITOR, "Hannah", "Mah", branch_id));
        accountModels.add(createAccount(TENANT, "Gregory","Mah", branch_id));
    }


    private AccountModel createAccount(String accountType, String firstName, String lastName, String branch_id){
        AccountModel newAccount = new AccountModel();
        newAccount.setAccount_id(0);
        newAccount.setBranch_id(branch_id);
        newAccount.setEmail("something@email.com");
        newAccount.setEmployee_id(123);
        newAccount.setFirst_name(firstName);
        newAccount.setLast_name(lastName);
        newAccount.setHp("234");
        newAccount.setUsername(firstName+lastName);
        newAccount.setRole_id(accountType);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(KNOWN_USER_PASSWORD);
        newAccount.setPassword(password);

        return newAccount;
    }


    private AccountModel createAccount(String accountType, String firstName, String lastName){
        return createAccount(accountType, firstName,lastName, "A");
    }

    private void createTenant(String first_name, String last_name, ArrayList<TenantModel> tenantModels, ArrayList<AccountModel> accountModels){
        TenantModel tenantModel = new TenantModel();
        tenantModel.setAcc_id(tenantModels.size());
        tenantModel.setAudit_score(10);
        tenantModel.setType_id("FB");
        tenantModel.setLatest_audit(0);
        tenantModel.setPast_audits(null);
        tenantModel.setBranch_id("A");
        tenantModel.setStore_addr("#01-02");
        tenantModels.add(tenantModel);
        accountModels.add(createAccount(TENANT,first_name,last_name));
    }

    private void createAuditor(String first_name, String last_name, ArrayList<AuditorModel> auditorModels, ArrayList<AccountModel> accountModels){
        AuditorModel auditorModel = new AuditorModel();
        auditorModel.setAcc_id(auditorModels.size());
        auditorModel.setCompleted_audits(null);
        auditorModel.setAppealed_audits(null);
        auditorModel.setOutstanding_audit_ids(null);
        auditorModel.setBranch_id("A");
        auditorModel.setMgr_id(0);
        auditorModels.add(auditorModel);
        accountModels.add(createAccount(AUDITOR,first_name,last_name));
    }

    private void createManager(String first_name, String last_name, ArrayList<ManagerModel> auditorModels, ArrayList<AccountModel> accountModels){
        ManagerModel managerModel = new ManagerModel();
        managerModel.setAcc_id(auditorModels.size());
        managerModel.setBranch_id("A");
        accountModels.add(createAccount(MANAGER,first_name,last_name));
    }

    private static class sampleAccounts{
        private final int acc_id;
        private final String username, firstName, lastName, branch_id;

        public sampleAccounts(int acc_id, String username, String firstName, String lastName, String branch_id) {
            this.acc_id = acc_id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.branch_id = branch_id;
        }

        public int getAcc_id() {
            return acc_id;
        }

        public String getUsername() {
            return username;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getBranch_id() {
            return branch_id;
        }
    }



}
