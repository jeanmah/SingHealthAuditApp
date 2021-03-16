package com.c2g4.SingHealthWebApp.Admin.Repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;

public interface AccountRepo extends CrudRepository<AccountModel, Integer>{
    @Query("SELECT * FROM Accounts WHERE username = :username LIMIT 1")
    AccountModel findByUsername(@Param("username") String username);

    @Query("SELECT * FROM Accounts WHERE account_id = :account_id LIMIT 1")
    AccountModel findByAccId(@Param("account_id") int account_id);

    @Query("SELECT role_id FROM Accounts WHERE username= :username")
    String getRoleFromUsername(@Param("username") String username);


}
