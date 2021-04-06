package com.c2g4.SingHealthWebApp.Admin.Repositories;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListSMAModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository of SQL queries to interact with the AuditCheckListSMARepo
 */
@Repository
public interface AuditCheckListSMARepo extends CrudRepository<AuditCheckListSMAModel, Integer>, AuditCheckListRepo {
    @Query("SELECT * FROM NFBCheckList")
    List<AuditCheckListSMAModel> getAllQuestions();

    @Query("SELECT * FROM NFBCheckList WHERE qn_id =:qn_id")
    AuditCheckListSMAModel getQuestion(@Param("qn_id") int qn_id);

    @Query("SELECT * from NFBCheckList where category = :category")
    List<AuditCheckListSMAModel> getQuestionByCategory(@Param("category") String category);

    @Override
    @Query("SELECT category FROM NFBCheckList WHERE nfb_qn_id= :nfb_qn_id")
    String getCategoryByQnID(@Param("fb_qn_id") int category);

    @Override
    @Query("SELECT weight FROM NFBCheckList WHERE nfb_qn_id= :nfb_qn_id")
    double getWeightByQnID(@Param("nfb_qn_id") int category);
}
