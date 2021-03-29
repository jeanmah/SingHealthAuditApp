package com.c2g4.SingHealthWebApp.ControllersTest;

import com.c2g4.SingHealthWebApp.Admin.Controllers.ReportController;
import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListFBModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListNFBModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReportController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReportControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuditCheckListFBRepo auditCheckListFBRepo;
    @MockBean
    private AuditCheckListNFBRepo auditCheckListNFBRepo;
    @MockBean
    private OpenAuditRepo openAuditRepo;
    @MockBean
    private CompletedAuditRepo completedAuditRepo;
    @MockBean
    private AccountRepo accountRepo;
    @MockBean
    private AuditorRepo auditorRepo;
    @MockBean
    private TenantRepo tenantRepo;
    @MockBean
    private ManagerRepo managerRepo;

    private static final String statusOK = "ok";
    private static final String statusBad = "bad";
    private static final String statusNotFound = "notFound";
    private static final String statusUnauthorized = "unauthorized";



    @Test
    public void testingTest(){

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

    private void getHttpNotFoundRequest(String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performGetRequest(requestURL,params);
        resultActions.andExpect(status().isNotFound());
    }

    private void getHttpUnauthorizedRequest(String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performGetRequest(requestURL,params);
        resultActions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void getAllQuestionsFB()
            throws Exception {
        List<AuditCheckListFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createFBChecklist(auditCheckListModels.size()));
        given(auditCheckListFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusOK,ResourceString.FB_KEY,"[{\"fb_qn_id\":0,\"category\":" +
                "\"cat\",\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"fb_qn_id\":1,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"fb_qn_id\":2,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"}]");
    }

    @Test
    public void getAllQuestionsNFB()
            throws Exception {
        List<AuditCheckListNFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        given(auditCheckListNFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusOK,ResourceString.NFB_KEY,"[{\"nfb_qn_id\":0,\"category\":" +
                "\"cat\",\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"nfb_qn_id\":1,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"nfb_qn_id\":2,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"}]");
    }

    @Test
    public void getAllQuestionsWrongInput()
            throws Exception {
        List<AuditCheckListNFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        given(auditCheckListNFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusNotFound,"wrongType",null);
    }

    @Test
    public void getAllQuestionsNoParam()
            throws Exception {
        List<AuditCheckListNFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        given(auditCheckListNFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getHttpBadRequest("/report/getAllQuestions", null);
    }

    private void getAllQuestions(String statusExpected, String checkListType, String compareJson) throws Exception {
        String url = "/report/getAllQuestions";
        HashMap<String,String> params = new HashMap<>(){{
            put("type",checkListType);
        }};
        switch (statusExpected) {
            case statusOK -> getHttpOk(url, params, 3, compareJson);
            case statusBad -> getHttpBadRequest(url, params);
            case statusUnauthorized -> getHttpUnauthorizedRequest(url, params);
            case statusNotFound -> getHttpNotFoundRequest(url,params);
        }
    }

    private void createArbitraryChecklists(String checklistType, List<AuditCheckListModel> auditCheckListModels){
        if(checklistType.equals(ResourceString.FB_KEY)){
            auditCheckListModels.add(createFBChecklist(auditCheckListModels.size()));
        } else if(checklistType.equals(ResourceString.NFB_KEY)){
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        }
    }

    private AuditCheckListFBModel createFBChecklist(int qn_id){
        AuditCheckListFBModel auditCheckListFBModel = new AuditCheckListFBModel();
        auditCheckListFBModel.setFb_qn_id(qn_id);
        auditCheckListFBModel.setCategory("cat");
        auditCheckListFBModel.setRequirement("sdf");
        auditCheckListFBModel.setSub_category("dog");
        auditCheckListFBModel.setWeight(0.2);
        auditCheckListFBModel.setSub_requirement("sfa");
        return auditCheckListFBModel;
    }

    private AuditCheckListNFBModel createNFBChecklist(int qn_id){
        AuditCheckListNFBModel auditCheckListNFBModel = new AuditCheckListNFBModel();
        auditCheckListNFBModel.setNfb_qn_id(qn_id);
        auditCheckListNFBModel.setCategory("cat");
        auditCheckListNFBModel.setRequirement("sdf");
        auditCheckListNFBModel.setSub_category("dog");
        auditCheckListNFBModel.setWeight(0.2);
        auditCheckListNFBModel.setSub_requirement("sfa");
        return auditCheckListNFBModel;
    }


}
