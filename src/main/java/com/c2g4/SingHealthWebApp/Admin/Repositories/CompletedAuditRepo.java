package com.c2g4.SingHealthWebApp.Admin.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;


@Repository
public interface CompletedAuditRepo extends CrudRepository<CompletedAuditModel, Integer> {


}