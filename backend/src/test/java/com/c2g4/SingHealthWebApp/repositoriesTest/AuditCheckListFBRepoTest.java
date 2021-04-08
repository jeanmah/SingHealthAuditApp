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
        List<AuditCheckListFBModel> auditCheckListFBModels = createAuditChecklistList();
        for(AuditCheckListFBModel a:auditCheckListFBModels){
            auditCheckListFBRepo.save(a);
        }
        List<AuditCheckListFBModel> actualList = auditCheckListFBRepo.getAllQuestions();
    }

    private boolean compareQuestions(AuditCheckListFBModel expected, AuditCheckListFBModel actual){
        boolean id = expected.getFb_qn_id()==actual.getFb_qn_id();
        boolean cat = expected.getCategory().equals(actual.getCategory());
        boolean subcat = expected.getSub_category().equals(actual.getSub_category());
        boolean weight = expected.getWeight() == actual.getWeight();
        boolean req = expected.getRequirement().equals(actual.getRequirement());
        boolean subreq = expected.getSub_requirement().equals(actual.getSub_requirement());
        return id && cat && subcat && weight && req && subreq;
    }

    private List<AuditCheckListFBModel> createAuditChecklistList(){
        List<AuditCheckListFBModel> auditCheckListFBModels = new ArrayList<>();
        for(int i=0;i<3;i++){
            auditCheckListFBModels.add(createChecklist());
        }
        return auditCheckListFBModels;
    }

    private AuditCheckListFBModel createChecklist(){
        AuditCheckListFBModel auditCheckListFBModel = new AuditCheckListFBModel();
        auditCheckListFBModel.setFb_qn_id(0);
        auditCheckListFBModel.setCategory(CATEGORY);
        auditCheckListFBModel.setSub_category(CATEGORY);
        auditCheckListFBModel.setWeight(0);
        auditCheckListFBModel.setRequirement("REQUIREMENT");
        auditCheckListFBModel.setSub_requirement("REQUIREMENT");
        return auditCheckListFBModel;
    }


//    @Query("SELECT * FROM FBCheckList")
//    List<AuditCheckListFBModel> getAllQuestions();
//
//    @Query("SELECT * FROM FBCheckList WHERE qn_id =:qn_id")
//    AuditCheckListNFBModel getQuestion(@Param("qn_id") int qn_id);
//
//    @Query("SELECT * FROM FBCheckList WHERE category = :category")
//    List<AuditCheckListFBModel> getQuestionByCategory(@Param("category") String category);
//
//    //Consider create a question class that stores all this info
//    //Might be more efficient for the DB by avoiding excessive queries
//    @Override
//    @Query("SELECT category FROM FBCheckList WHERE fb_qn_id= :fb_qn_id")
//    String getCategoryByQnID(@Param("fb_qn_id") int fb_qn_id);
//
//    @Override
//    @Query("SELECT weight FROM FBCheckList WHERE fb_qn_id= :fb_qn_id")
//    double getWeightByQnID(@Param("fb_qn_id") int fb_qn_id);
}
