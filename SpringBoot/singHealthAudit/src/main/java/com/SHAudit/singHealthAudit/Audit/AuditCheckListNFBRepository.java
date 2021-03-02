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
}
