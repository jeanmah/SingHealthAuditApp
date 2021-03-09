package com.c2g4.SingHealthWebApp.Admin.Repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;

@Repository
public interface ManagerRepo extends CrudRepository<ManagerModel, Integer> {

    @Query("SELECT acc_id FROM Managers WHERE branch_id= :branch_id")
    int getManagerIdFromBranchId(@Param("branch_id") String branch_id);

}
