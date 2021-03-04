package com.SHAudit.singHealthAudit.Audit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedAuditsRepository extends CrudRepository<CompletedAudits, Integer> {


}
