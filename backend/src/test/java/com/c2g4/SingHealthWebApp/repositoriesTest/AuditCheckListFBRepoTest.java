package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListFBModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListNFBModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public class AuditCheckListFBRepoTest {
    @Autowired
    private AuditCheckListFBRepo auditCheckListFBRepo;

    private static final int ACCOUNT_ID = 9000;
    private static final String CATEGORY = "CATEGORY";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String BRANCHID = "CGH";

    @BeforeEach
    public void clearRepo(){
        auditCheckListFBRepo.deleteAll();
    }

    @Test
    public void getAllQuestions(){
        List<AuditCheckListFBModel> auditCheckListFBModels = createAndSaveList();
        List<AuditCheckListFBModel> actualList = auditCheckListFBRepo.getAllQuestions();
        assert(auditCheckListFBModels.size()==actualList.size());
        for(int i=0;i<auditCheckListFBModels.size();i++){
            assert(compareQuestions(auditCheckListFBModels.get(i),actualList.get(i)));
        }
    }

    @Test
    public void getAllQuestionsNoQuestions(){
        List<AuditCheckListFBModel> actualList = auditCheckListFBRepo.getAllQuestions();
        assert(actualList.size()==0);
    }

    @Test
    public void getQuestion(){
        List<AuditCheckListFBModel> expectedModels = createAndSaveList();
        AuditCheckListFBModel actual = auditCheckListFBRepo.getQuestion(expectedModels.get(0).getFb_qn_id());
        assert(compareQuestions(expectedModels.get(0),actual));
    }

    @Test
    public void getQuestionNotFound(){
        AuditCheckListFBModel actual = auditCheckListFBRepo.getQuestion(-10);
        assert(actual ==null);
    }

    @Test
    public void getQuestionByCategory(){
        String thisCat = "thisCat";
        createAndSaveList();
        List<AuditCheckListFBModel> auditCheckListFBModelsThisCat = createAndSaveList(thisCat);
        List<AuditCheckListFBModel> actualList = auditCheckListFBRepo.getQuestionByCategory(thisCat);
        assert(auditCheckListFBModelsThisCat.size()==actualList.size());
        for(int i=0;i<actualList.size();i++){
            assert(compareQuestions(actualList.get(i),actualList.get(i)));
        }
    }

    @Test
    public void getQuestionByCategoryNoQuestions(){
        List<AuditCheckListFBModel> actualList = auditCheckListFBRepo.getAllQuestions();
        assert(actualList.size()==0);
    }

//    @Query("SELECT * FROM FBCheckList WHERE category = :category")
//    List<AuditCheckListFBModel> getQuestionByCategory(@Param("category") String category);
//
//    @Override
//    @Query("SELECT category FROM FBCheckList WHERE fb_qn_id= :fb_qn_id")
//    String getCategoryByQnID(@Param("fb_qn_id") int fb_qn_id);
//
//    @Override
//    @Query("SELECT weight FROM FBCheckList WHERE fb_qn_id= :fb_qn_id")
//    double getWeightByQnID(@Param("fb_qn_id") int fb_qn_id);

    private boolean compareQuestions(AuditCheckListFBModel expected, AuditCheckListFBModel actual){
        boolean id = expected.getFb_qn_id()==actual.getFb_qn_id();
        boolean cat = expected.getCategory().equals(actual.getCategory());
        boolean subcat = expected.getSub_category().equals(actual.getSub_category());
        boolean weight = expected.getWeight() == actual.getWeight();
        boolean req = expected.getRequirement().equals(actual.getRequirement());
        boolean subreq = expected.getSub_requirement().equals(actual.getSub_requirement());
        return id && cat && subcat && weight && req && subreq;
    }

    private List<AuditCheckListFBModel> createAuditChecklistList(String category){
        List<AuditCheckListFBModel> auditCheckListFBModels = new ArrayList<>();
        for(int i=0;i<3;i++){
            auditCheckListFBModels.add(createChecklist(category));
        }
        return auditCheckListFBModels;
    }
    private List<AuditCheckListFBModel> createAndSaveList(){
        return  createAndSaveList(CATEGORY);
    }
    private List<AuditCheckListFBModel> createAndSaveList(String category){
        List<AuditCheckListFBModel> auditCheckListFBModels = createAuditChecklistList(category);
        List<AuditCheckListFBModel> auditCheckListFBModelsUpdated = new ArrayList<>();
        for (AuditCheckListFBModel auditCheckListFBModel : auditCheckListFBModels) {
            auditCheckListFBModelsUpdated.add(auditCheckListFBRepo.save(auditCheckListFBModel));
        }
        return auditCheckListFBModelsUpdated;
    }

    private AuditCheckListFBModel createChecklist(String category){
        AuditCheckListFBModel auditCheckListFBModel = new AuditCheckListFBModel();
        auditCheckListFBModel.setFb_qn_id(0);
        auditCheckListFBModel.setCategory(category);
        auditCheckListFBModel.setSub_category(category);
        auditCheckListFBModel.setWeight(0);
        auditCheckListFBModel.setRequirement("REQUIREMENT");
        auditCheckListFBModel.setSub_requirement("REQUIREMENT");
        return auditCheckListFBModel;
    }
}
