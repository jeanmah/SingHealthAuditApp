package com.SHAudit.singHealthAudit.Audit;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenAuditsRepository extends CrudRepository<OpenAudits, Long> {
}