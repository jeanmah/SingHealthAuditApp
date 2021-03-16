package com.c2g4.SingHealthWebApp.Admin.Repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;


@Repository
public interface CompletedAuditRepo extends CrudRepository<CompletedAuditModel, Integer> {
    @Query("SELECT * FROM Open_Audits WHERE report_id = :report_id LIMIT 1")
    CompletedAuditModel getCompletedAuditById(@Param("report_id") int report_id);
}