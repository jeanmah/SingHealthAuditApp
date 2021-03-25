package com.c2g4.SingHealthWebApp.Admin.Repositories;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;

import java.util.List;

@Repository
public interface ManagerRepo extends CrudRepository<ManagerModel, Integer> {


    @Query("SELECT * FROM Managers WHERE acc_id = :acc_id LIMIT 1")
    ManagerModel getManagerById(@Param("acc_id") int acc_id);

    @Query("SELECT acc_id FROM Managers WHERE branch_id= :branch_id")
    int getManagerIdFromBranchId(@Param("branch_id") String branch_id);

    @Query("SELECT * FROM Managers")
    List<ManagerModel> getAllManagers();

}
