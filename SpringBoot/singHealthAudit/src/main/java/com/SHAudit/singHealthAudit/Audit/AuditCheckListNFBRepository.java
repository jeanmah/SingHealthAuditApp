package com.SHAudit.singHealthAudit.Audit;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditCheckListNFBRepository extends CrudRepository<AuditCheckListNFB, Integer> {
    @Query("SELECT * from NFBCheckList where category = :category")
    List<AuditCheckListNFB> getQuestionByCategory(@Param("category") String category);

    @Query("SELECT category FROM NFBCheckList WHERE nfb_qn_id= :nfb_qn_id")
    String getCategoryByQnID(@Param("fb_qn_id") int category);

    @Query("SELECT weight FROM NFBCheckList WHERE nfb_qn_id= :nfb_qn_id")
    double getWeightByQnID(@Param("nfb_qn_id") int category);

}
