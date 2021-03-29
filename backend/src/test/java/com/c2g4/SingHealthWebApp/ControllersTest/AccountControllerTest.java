package com.c2g4.SingHealthWebApp.ControllersTest;

import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;

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

    private static final int MANAGERID = 100;
    private static final int AUDITORID = 100;
    private static final int TENANTID = 100;

    private static final String statusOK = "ok";
    private static final String statusBad = "bad";
    private static final String statusUnauthorized = "unauthorized";

    @Before
    public void before() {
        AccountModel managerAccount = createAccount(MANAGER,MANAGERID,"Marcus","Ho","HQ");
        AccountModel auditorAccount = createAccount(AUDITOR,AUDITORID,"Hannah","Mah","Branch_A");
        AccountModel tenantAccount = createAccount(TENANT,TENANTID,"Gregory","Mah","Branch_A");
        given(accountRepo.findByUsername(MANAGERUSENAME)).willReturn(managerAccount);
        given(accountRepo.findByUsername(AUDITORUSENAME)).willReturn(auditorAccount);
        given(accountRepo.findByUsername(TENANTUSENAME)).willReturn(tenantAccount);
    }

    private ResultActions performGetRequest(String requestURL, HashMap<String,String> params) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(requestURL)
                                                                            .contentType(MediaType.APPLICATION_JSON);
        if(params!=null){
            for(String key:params.keySet()){
                mockHttpServletRequestBuilder.param(key,params.get(key));
            }
        }
        return mvc.perform(mockHttpServletRequestBuilder);
    }

    private void getHttpOk(String requestURL, HashMap<String,String> params, int jsonSize, String compareJson) throws Exception {
        ResultActions resultActions = performGetRequest(requestURL,params);
        resultActions.andExpect(status().isOk());
        if(jsonSize!=-1) resultActions.andExpect(jsonPath("$",hasSize(jsonSize)));
        if(compareJson!=null){
            resultActions.andExpect(content().json(compareJson));
        }
    }

    private void getHttpBadRequest(String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performGetRequest(requestURL,params);
        resultActions.andExpect(status().isBadRequest());
    }

    private void getHttpUnauthorizedRequest(String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performGetRequest(requestURL,params);
        resultActions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    private ResultActions performPostRequest(String url, String bodyString) throws Exception {
        MockMultipartFile postFile = new MockMultipartFile("changes","changes",MediaType.APPLICATION_JSON_VALUE,bodyString.getBytes());
        return mvc.perform(MockMvcRequestBuilders.multipart(url).file(postFile));
    }

    private void postHttpOK(String requestURL, String bodyString) throws Exception{
        ResultActions resultActions = performPostRequest(requestURL,bodyString);
        resultActions.andExpect(status().isOk());
    }

    private void postHttpBadRequest(String requestURL, String bodyString) throws Exception{
        ResultActions resultActions = performPostRequest(requestURL,bodyString);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersAsManagerWithResults()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createAribraryUsers(allAccounts);
        given(accountRepo.getAllAccounts()).willReturn(allAccounts);
        getAllUsers(statusOK,"[{\"acc_id\":0,\"first_name\":\"Marcus\",\"last_name\":\"Ho\"," +
                "\"role_id\":\"Manager\"},{\"acc_id\":0,\"first_name\":\"Hannah\",\"last_name\":\"Mah\"," +
                "\"role_id\":\"Auditor\"},{\"acc_id\":0,\"first_name\":\"Gregory\",\"last_name\":\"Mah\"," +
                "\"role_id\":\"Tenant\"}]\n");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersAsManagerNoResults()
            throws Exception {
        given(accountRepo.getAllAccounts()).willReturn(null);
        getAllUsers(statusBad,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersAsAuditor()
            throws Exception {
//        ArrayList<AccountModel> allAccounts = new ArrayList<>();
//        createAribraryUsers(allAccounts);
//        given(accountRepo.getAllAccounts()).willReturn(allAccounts);
        getAllUsers(statusUnauthorized,null);

    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllUsersAsTenant()
            throws Exception {
//        ArrayList<AccountModel> allAccounts = new ArrayList<>();
//        createAribraryUsers(allAccounts);
//        given(accountRepo.getAllAccounts()).willReturn(allAccounts);
        getAllUsers(statusUnauthorized,null);
    }

    private void getAllUsers(String statusExpected, String compareJson) throws Exception {
        String url = "/account/getAllUsers";
        switch (statusExpected) {
            case statusOK -> getHttpOk(url, null, 3, compareJson);
            case statusBad -> getHttpBadRequest(url, null);
            case statusUnauthorized -> getHttpUnauthorizedRequest(url, null);
        }
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofBranchAsManagerWithResults()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createArbitraySameBranchUsers(allAccounts, "Branch_A");
        given(accountRepo.getAllAccountsByBranchId("Branch_A")).willReturn(allAccounts);
        getAllUsersOfBranch(statusOK,"Branch_A","[{\"acc_id\":0,\"first_name\":\"Marcus\"," +
                "\"last_name\":\"Ho\",\"role_id\":\"Manager\"},{\"acc_id\":0,\"first_name\":\"Hannah\"," +
                "\"last_name\":\"Mah\",\"role_id\":\"Auditor\"},{\"acc_id\":0,\"first_name\":\"Gregory\"," +
                "\"last_name\":\"Mah\",\"role_id\":\"Tenant\"}]\n");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofBranchAsManagerNoResults()
            throws Exception {
        given(accountRepo.getAllAccountsByBranchId("Branch_A")).willReturn(null);
        getAllUsersOfBranch(statusBad,"Branch_A",null);

    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofBranchAsAuditor()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createArbitraySameBranchUsers(allAccounts, "Branch_A");
        given(accountRepo.getAllAccountsByBranchId("Branch_A")).willReturn(allAccounts);
        getAllUsersOfBranch(statusUnauthorized,"Branch_A",null);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllUsersofBranchAsTenant()
            throws Exception {
        ArrayList<AccountModel> allAccounts = new ArrayList<>();
        createArbitraySameBranchUsers(allAccounts, "Branch_A");
        given(accountRepo.getAllAccountsByBranchId("Branch_A")).willReturn(allAccounts);
        getAllUsersOfBranch(statusUnauthorized,"Branch_A",null);
    }

    private void getAllUsersOfBranch(String statusExpected, String branch_id, String compareJson) throws Exception {
        String url = "/account/getAllUsersofBranch";
        HashMap<String,String> params = new HashMap<>(){{
            put("branch_id",branch_id);
        }};
        switch (statusExpected) {
            case statusOK -> getHttpOk(url, params, 3, compareJson);
            case statusBad -> getHttpBadRequest(url, params);
            case statusUnauthorized -> getHttpUnauthorizedRequest(url, params);
        }
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeTenantAsManagerWithResults()
            throws Exception {
        ArrayList<TenantModel> tenantModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryTenants(tenantModels, accountModels);
        given(tenantRepo.getAllTenants()).willReturn(tenantModels);
        getAllUsersOfType(statusOK,TENANT,accountModels,"[{\"employee_id\":123,\"username\":\"Johndoh\",\"first_name\":\"John\"," +
                "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\"," +
                "\"branch_id\":\"Branch_A\",\"acc_id\":0,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0," +
                "\"past_audits\":null,\"store_addr\":\"#01-02\"},{\"employee_id\":123,\"username\":\"Marydoh\"," +
                "\"first_name\":\"Mary\",\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\"," +
                "\"role_id\":\"Tenant\",\"branch_id\":\"Branch_A\",\"acc_id\":1,\"type_id\":\"FB\"," +
                "\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}," +
                "{\"employee_id\":123,\"username\":\"Pauldoh\",\"first_name\":\"Paul\",\"last_name\":\"doh\"," +
                "\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"Branch_A\"," +
                "\"acc_id\":2,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null," +
                "\"store_addr\":\"#01-02\"}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeTenantAsManagerNoResults()
            throws Exception {
        given(tenantRepo.getAllTenants()).willReturn(null);
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(null);
        getAllUsersOfType(statusBad,TENANT,null,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeAuditorAsManagerWithResults()
            throws Exception {
        ArrayList<AuditorModel> auditorModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryAuditors(auditorModels, accountModels);
        given(auditorRepo.getAllAuditors()).willReturn(auditorModels);
        getAllUsersOfType(statusOK,AUDITOR,accountModels,"[{\"employee_id\":123,\"username\":\"Johndoh\"," +
                "\"first_name\":\"John\",\"last_name\":\"doh\",\"email\":\"something@email.com\"," +
                "\"hp\":\"234\",\"role_id\":\"Auditor\",\"branch_id\":\"Branch_A\",\"acc_id\":0," +
                "\"completed_audits\":null,\"appealed_audits\":null,\"outstanding_audit_ids\":null," +
                "\"mgr_id\":0},{\"employee_id\":123,\"username\":\"Marydoh\",\"first_name\":\"Mary\"," +
                "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\"," +
                "\"role_id\":\"Auditor\",\"branch_id\":\"Branch_A\",\"acc_id\":1,\"completed_audits\":null," +
                "\"appealed_audits\":null,\"outstanding_audit_ids\":null,\"mgr_id\":0},{\"employee_id\":123," +
                "\"username\":\"Pauldoh\",\"first_name\":\"Paul\",\"last_name\":\"doh\"," +
                "\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Auditor\"," +
                "\"branch_id\":\"Branch_A\",\"acc_id\":2,\"completed_audits\":null," +
                "\"appealed_audits\":null,\"outstanding_audit_ids\":null,\"mgr_id\":0}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeAuditorAsManagerNoResults()
            throws Exception {
        given(auditorRepo.getAllAuditors()).willReturn(null);
        given(accountRepo.getAllAccountsByBranchId("A")).willReturn(null);
        getAllUsersOfType(statusBad,AUDITOR,null,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeManagerAsManagerWithResults()
            throws Exception {
        ArrayList<ManagerModel> managerModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryManagers(managerModels, accountModels);
        given(managerRepo.getAllManagers()).willReturn(managerModels);
        getAllUsersOfType(statusOK,MANAGER,accountModels,"[{\"employee_id\":123,\"username\":\"Johndoh\",\"first_name\":\"John\"," +
                "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Manager\"," +
                "\"branch_id\":\"Branch_A\",\"acc_id\":0},{\"employee_id\":123,\"username\":\"Marydoh\",\"first_name\":\"Mary\"," +
                "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Manager\"," +
                "\"branch_id\":\"Branch_A\",\"acc_id\":1},{\"employee_id\":123,\"username\":\"Pauldoh\"," +
                "\"first_name\":\"Paul\",\"last_name\":\"doh\",\"email\":\"something@email.com\"," +
                "\"hp\":\"234\",\"role_id\":\"Manager\",\"branch_id\":\"Branch_A\",\"acc_id\":2}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllUsersofTypeManagerAsManagerNoResults()
            throws Exception {
        given(managerRepo.getAllManagers()).willReturn(null);
        getAllUsersOfType(statusBad,MANAGER,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofTypeTenantAsAuditor()
            throws Exception {
        ArrayList<TenantModel> tenantModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        createArbitraryTenants(tenantModels, accountModels);
        System.out.println("number of tenants" + tenantModels.size());
        given(tenantRepo.getAllTenantsByBranchId("Branch_A")).willReturn(tenantModels);
        getAllUsersOfType(statusOK,TENANT,accountModels,"[{\"employee_id\":123,\"username\":\"Johndoh\",\"first_name\":\"John\"," +
                "\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\"," +
                "\"branch_id\":\"Branch_A\",\"acc_id\":0,\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0," +
                "\"past_audits\":null,\"store_addr\":\"#01-02\"},{\"employee_id\":123,\"username\":\"Marydoh\"," +
                "\"first_name\":\"Mary\",\"last_name\":\"doh\",\"email\":\"something@email.com\",\"hp\":\"234\"," +
                "\"role_id\":\"Tenant\",\"branch_id\":\"Branch_A\",\"acc_id\":1,\"type_id\":\"FB\",\"audit_score\":10," +
                "\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"},{\"employee_id\":123," +
                "\"username\":\"Pauldoh\",\"first_name\":\"Paul\",\"last_name\":\"doh\",\"email\":\"something@email.com\"," +
                "\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"Branch_A\",\"acc_id\":2," +
                "\"type_id\":\"FB\",\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofTypeTenantAsAuditorNoResults()
            throws Exception {
        given(tenantRepo.getAllTenantsByBranchId("Branch_A")).willReturn(null);
        getAllUsersOfType(statusBad,TENANT,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllUsersofTypeNotTenantAsAuditorNoResults()
            throws Exception {
        getAllUsersOfType(statusUnauthorized,AUDITOR,null,null);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllUsersofTypeAsTenant()
            throws Exception {
        getAllUsersOfType(statusUnauthorized,TENANT,null,null);
    }

    private void getAllUsersOfType(String statusExpected, String roleType, ArrayList<AccountModel> accountModels, String compareJson) throws Exception {
        String url = "/account/getAllUsersofType";
        HashMap<String,String> params = new HashMap<>(){{
            put("roleType",roleType);
        }};
        switch (statusExpected) {
            case statusOK -> {
                given(accountRepo.findByAccId(0)).willReturn(accountModels.get(0));
                given(accountRepo.findByAccId(1)).willReturn(accountModels.get(1));
                given(accountRepo.findByAccId(2)).willReturn(accountModels.get(2));
                getHttpOk(url, params, 3, compareJson);
            }
            case statusBad -> getHttpBadRequest(url, params);
            case statusUnauthorized -> getHttpUnauthorizedRequest(url, params);
        }
    }


    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileTenantAsManagerUserIDWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(TENANT,"Bob","Bobby","Branch_A");
        TenantModel tenantModel = createTenant(0,"FB","Branch_A");
        given(accountRepo.findByAccId(0)).willReturn(accountModel);
        given(tenantRepo.getTenantById(0)).willReturn(tenantModel);
        getUserProfile(statusOK,0,null,null,"{\"employee_id\":123,\"username\":\"BobBobby\"," +
                "\"first_name\":\"Bob\",\"last_name\":\"Bobby\",\"email\":\"something@email.com\"," +
                "\"hp\":\"234\",\"role_id\":\"Tenant\",\"branch_id\":\"Branch_A\",\"acc_id\":0," +
                "\"type_id\":\"FB\",\"audit_score\":10," +
                "\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileTenantAsManagerUserIDNoResults()
            throws Exception {
        given(accountRepo.findByAccId(0)).willReturn(null);
        getUserProfile(statusBad,0,null,null,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileAuditorAsManagerUserIDWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(AUDITOR,"Bob","Bobby","Branch_A");
        AuditorModel auditorModel = createAuditor(0,"Branch_A");
        given(accountRepo.findByAccId(0)).willReturn(accountModel);
        given(auditorRepo.getAuditorById(0)).willReturn(auditorModel);
        getUserProfile(statusOK,0,null,null,"{\"employee_id\":123,\"username\":\"BobBobby\",\"first_name\":\"Bob\"," +
                "\"last_name\":\"Bobby\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Auditor\"," +
                "\"branch_id\":\"Branch_A\",\"acc_id\":0," + "\"completed_audits\":null,\"appealed_audits\":null," +
                "\"outstanding_audit_ids\":null,\"mgr_id\":0}");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileAuditorAsManagerUserIDNoResults()
            throws Exception {
        given(accountRepo.findByAccId(0)).willReturn(null);
        getUserProfile(statusBad,0,null,null,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileManagerAsManagerUserIDWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(MANAGER,"Bob","Bobby","Branch_A");
        ManagerModel managerModel = createManager(0,"Branch_A");
        given(accountRepo.findByAccId(0)).willReturn(accountModel);
        given(managerRepo.getManagerById(0)).willReturn(managerModel);
        getUserProfile(statusOK,0,null,null,"{\"employee_id\":123,\"username\":\"BobBobby\"," +
                "\"first_name\":\"Bob\",\"last_name\":\"Bobby\",\"email\":\"something@email.com\"," +
                "\"hp\":\"234\",\"role_id\":\"Manager\",\"branch_id\":\"Branch_A\",\"acc_id\":0}");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileManagerAsManagerUserIDNoResults()
            throws Exception {
        given(accountRepo.findByAccId(0)).willReturn(null);
        getUserProfile(statusBad,0,null,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileTenantSameBranchAsAuditorUserIDWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(TENANT,"Bob","Bobby","Branch_A");
        TenantModel tenantModel = createTenant(0,"FB","Branch_A");
        given(accountRepo.findByAccId(0)).willReturn(accountModel);
        given(tenantRepo.getTenantById(0)).willReturn(tenantModel);
        getUserProfile(statusOK,0,null,null,"{\"employee_id\":123,\"username\":\"BobBobby\",\"first_name\":\"Bob\"," +
                "\"last_name\":\"Bobby\",\"email\":\"something@email.com\",\"hp\":\"234\"," +
                "\"role_id\":\"Tenant\",\"branch_id\":\"Branch_A\",\"acc_id\":0,\"type_id\":\"FB\"," +
                "\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileTenantSameBranchAsAuditorUserIDWNoResults()
            throws Exception {
        given(accountRepo.findByAccId(0)).willReturn(null);
        getUserProfile(statusBad,0,null,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileTenantDiffBranchAsAuditorUserIDWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(TENANT,"Bob","Bobby","Branch_B");
        given(accountRepo.findByAccId(1)).willReturn(accountModel);
        getUserProfile(statusUnauthorized,1,null,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileTenantDiffBranchAsAuditorUserIDWNoResults()
            throws Exception {
        given(accountRepo.findByAccId(0)).willReturn(null);
        getUserProfile(statusBad,0,null,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileNotTenantAsAuditorUserIDWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(AUDITOR,"Bob","Bobby","Branch_A");
        given(accountRepo.findByAccId(1)).willReturn(accountModel);
        getUserProfile(statusUnauthorized,1,null,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileNotTenantAsAuditorUserIDNoResults()
            throws Exception {
        given(accountRepo.findByAccId(0)).willReturn(null);
        getUserProfile(statusBad,0,null,null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileSelfAsAuditorUserID()
            throws Exception {
        AccountModel accountModel = createAccount(AUDITOR,AUDITORID,"Hannah","Mah","Branch_A");
        AuditorModel auditorModel = createAuditor(AUDITORID,"Branch_A");
        given(accountRepo.findByAccId(AUDITORID)).willReturn(accountModel);
        given(auditorRepo.getAuditorById(AUDITORID)).willReturn(auditorModel);
        getUserProfile(statusOK,AUDITORID,null,null,"{\"employee_id\":123,\"username\":\"HannahMah\",\"first_name\":\"Hannah\"," +
                "\"last_name\":\"Mah\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Auditor\",\"branch_id\":\"Branch_A\"," +
                "\"acc_id\":100,\"completed_audits\":null,\"appealed_audits\":null,\"outstanding_audit_ids\":null,\"mgr_id\":0}");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getUserProfileNotSelfAsTenantUserIDWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(TENANT,1,"Bob","Bobby");
        given(accountRepo.findByAccId(1)).willReturn(accountModel);
        getUserProfile(statusUnauthorized,1,null,null,null);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getUserProfileNotSelfAsTenantUserIDNoResults()
            throws Exception {
        AccountModel accountModel = createAccount(TENANT,1, "Bob","Bobby");
        given(accountRepo.findByAccId(1)).willReturn(accountModel);
        getUserProfile(statusUnauthorized,1,null,null,null);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getUserProfileSelfAsTenantUserID()
            throws Exception {
        AccountModel accountModel = createAccount(TENANT,"Hannah","Mah","Branch_A");
        TenantModel tenantModel = createTenant(TENANTID,"FB","Branch_A");
        given(accountRepo.findByAccId(TENANTID)).willReturn(accountModel);
        given(tenantRepo.getTenantById(TENANTID)).willReturn(tenantModel);
        getUserProfile(statusOK,TENANTID,null,null,"{\"employee_id\":123,\"username\":\"HannahMah\",\"first_name\":\"Hannah\"," +
                "\"last_name\":\"Mah\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Tenant\"," +
                "\"branch_id\":\"Branch_A\",\"acc_id\":100,\"type_id\":\"FB\"," +
                "\"audit_score\":10,\"latest_audit\":0,\"past_audits\":null,\"store_addr\":\"#01-02\"}\n");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileBothNamesWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(MANAGER,"Bob","Bobby","Branch_A");
        ManagerModel managerModel = createManager(0,"Branch_A");
        given(accountRepo.getAccIdFromNames("Bob","Bobby")).willReturn(1);
        given(accountRepo.findByAccId(1)).willReturn(accountModel);
        given(managerRepo.getManagerById(1)).willReturn(managerModel);

        getUserProfile(statusOK,-1,"Bob","Bobby","{\"employee_id\":123,\"username\":\"BobBobby\"," +
                "\"first_name\":\"Bob\",\"last_name\":\"Bobby\",\"email\":\"something@email.com\"," +
                "\"hp\":\"234\",\"role_id\":\"Manager\",\"branch_id\":\"Branch_A\",\"acc_id\":0}");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileFirstNameOnlyNoResults()
            throws Exception {
        getUserProfile(statusBad,-1,"Bob",null,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getUserProfileLastNameOnlyNoResults()
            throws Exception {
        getUserProfile(statusBad,-1,null,"Bob",null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getUserProfileNoParamWithResults()
            throws Exception {
        AccountModel accountModel = createAccount(AUDITOR,AUDITORID,"Hannah","Mah","Branch_A");
        AuditorModel auditorModel = createAuditor(AUDITORID,"Branch_A");
        given(accountRepo.findByAccId(AUDITORID)).willReturn(accountModel);
        given(auditorRepo.getAuditorById(AUDITORID)).willReturn(auditorModel);
        getUserProfile(statusOK,AUDITORID,null,null,"{\"employee_id\":123,\"username\":\"HannahMah\",\"first_name\":\"Hannah\"," +
                "\"last_name\":\"Mah\",\"email\":\"something@email.com\",\"hp\":\"234\",\"role_id\":\"Auditor\",\"branch_id\":\"Branch_A\"," +
                "\"acc_id\":100,\"completed_audits\":null,\"appealed_audits\":null,\"outstanding_audit_ids\":null,\"mgr_id\":0}");
    }

    private void getUserProfile(String statusExpected, int userID,String firstName, String lastName, String compareJson) throws Exception {
        String url = "/account/getUserProfile";
        HashMap<String,String> params = new HashMap<>();
        if(userID!=-1) params.put("user_id",String.valueOf(userID));
        if(firstName!=null) params.put("firstName",firstName);
        if(lastName!=null) params.put("lastName",lastName);
        switch (statusExpected) {
            case statusOK -> getHttpOk(url, params, -1, compareJson);
            case statusBad -> getHttpBadRequest(url, params);
            case statusUnauthorized -> getHttpUnauthorizedRequest(url, params);
        }
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postProfileUpdateOK()
            throws Exception {
        postProfileUpdate(statusOK,"username","firstname","lastname","email","123");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postProfileUpdateMissingField()
            throws Exception {
        postProfileUpdate(statusBad,"username","firstname","lastname","email",null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postProfileUpdateNULLField()
            throws Exception {
        String hp = null;
        postProfileUpdate(statusBad,"username","firstname","lastname","email",hp);
    }

    private void postProfileUpdate(String statusExpected, String username, String firstName, String lastName, String email, String hp) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode checklistNode = mapper.createObjectNode();
        checklistNode.put("username", username);
        checklistNode.put("first_name", firstName);
        checklistNode.put("last_name", lastName);
        checklistNode.put("email", email);
        if(hp!=null) checklistNode.put("hp", hp);
        String bodyString = mapper.writeValueAsString(checklistNode);

        String url = "/account/postProfileUpdate";
        switch (statusExpected) {
            case statusOK -> postHttpOK(url,bodyString );
            case statusBad -> postHttpBadRequest(url, bodyString);
        }
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postPasswordUpdateOK()
            throws Exception {
        postPasswordUpdate(statusBad,"newPassword");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postPasswordUpdateNullBody()
            throws Exception {
        postPasswordUpdate(statusBad,JSONObject.NULL.toString());
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postPasswordUpdateEmptyPassword()
            throws Exception {
        postPasswordUpdate(statusBad,"");
    }

    private void postPasswordUpdate(String statusExpected, String password) throws Exception {
        String url = "/account/postPasswordUpdate";
        switch (statusExpected) {
            case statusOK -> postHttpOK(url,password);
            case statusBad -> postHttpBadRequest(url, password);
        }
    }



    private void createArbitraryTenants(ArrayList<TenantModel> tenantModels, ArrayList<AccountModel> accountModels){
        createTenantWithAccount("John","doh",tenantModels,accountModels);
        createTenantWithAccount("Mary","doh",tenantModels,accountModels);
        createTenantWithAccount("Paul","doh",tenantModels,accountModels);
    }

    private void createArbitraryAuditors(ArrayList<AuditorModel> auditorModels, ArrayList<AccountModel> accountModels){
        createAuditorWithAccount("John","doh",auditorModels,accountModels);
        createAuditorWithAccount("Mary","doh",auditorModels,accountModels);
        createAuditorWithAccount("Paul","doh",auditorModels,accountModels);
    }

    private void createArbitraryManagers(ArrayList<ManagerModel> managerModels, ArrayList<AccountModel> accountModels){
        createManagerWithAccount("John","doh",managerModels,accountModels);
        createManagerWithAccount("Mary","doh",managerModels,accountModels);
        createManagerWithAccount("Paul","doh",managerModels,accountModels);
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


    private AccountModel createAccount(String accountType, int acc_id, String firstName, String lastName, String branch_id){
        AccountModel newAccount = new AccountModel();
        newAccount.setAccount_id(acc_id);
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

    private AccountModel createAccount(String accountType, int acc_id, String firstName, String lastName){
        return createAccount(accountType, acc_id, firstName,lastName, "A");
    }

    private AccountModel createAccount(String accountType, String firstName, String lastName, String branch_id){
        return createAccount(accountType, 0, firstName,lastName, branch_id);
    }

    private AccountModel createAccount(String accountType, String firstName, String lastName){
        return createAccount(accountType, 0, firstName,lastName, "A");
    }

    private TenantModel createTenant(int acc_id, String type, String branch_id){
        TenantModel tenantModel = new TenantModel();
        tenantModel.setAcc_id(acc_id);
        tenantModel.setAudit_score(10);
        tenantModel.setType_id(type);
        tenantModel.setLatest_audit(0);
        tenantModel.setPast_audits(null);
        tenantModel.setBranch_id(branch_id);
        tenantModel.setStore_addr("#01-02");
        return tenantModel;
    }

    private AuditorModel createAuditor(int acc_id, String branch_id){
        AuditorModel auditorModel = new AuditorModel();
        auditorModel.setAcc_id(acc_id);
        auditorModel.setCompleted_audits(null);
        auditorModel.setAppealed_audits(null);
        auditorModel.setOutstanding_audit_ids(null);
        auditorModel.setBranch_id(branch_id);
        auditorModel.setMgr_id(0);
        return auditorModel;
    }

    private ManagerModel createManager(int acc_id, String branch_id){
        ManagerModel managerModel = new ManagerModel();
        managerModel.setAcc_id(acc_id);
        managerModel.setBranch_id(branch_id);
        return managerModel;
    }

    private void createTenantWithAccount(String first_name, String last_name, ArrayList<TenantModel> tenantModels, ArrayList<AccountModel> accountModels){

        tenantModels.add(createTenant(tenantModels.size(),"FB","Branch_A"));
        accountModels.add(createAccount(TENANT,first_name,last_name));
    }

    private void createAuditorWithAccount(String first_name, String last_name, ArrayList<AuditorModel> auditorModels, ArrayList<AccountModel> accountModels){

        auditorModels.add(createAuditor(auditorModels.size(),"Branch_A"));
        accountModels.add(createAccount(AUDITOR,first_name,last_name));
    }


    private void createManagerWithAccount(String first_name, String last_name, ArrayList<ManagerModel> managerModels, ArrayList<AccountModel> accountModels){
        managerModels.add(createManager(managerModels.size(),"Branch_A"));
        accountModels.add(createAccount(MANAGER,first_name,last_name));
    }
}
