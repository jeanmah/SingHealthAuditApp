package com.SHAudit.singHealthAudit.Audit;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditCheckListFBRepository extends CrudRepository<AuditCheckListFB, Integer> {
    @Query("SELECT * FROM FBCheckList WHERE category = :category")
    List<AuditCheckListFB> getQuestionByCategory(@Param("category") String category);
}
