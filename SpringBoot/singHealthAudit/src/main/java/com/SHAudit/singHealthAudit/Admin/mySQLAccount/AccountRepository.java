package com.SHAudit.singHealthAudit.Admin.mySQLAccount;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    @Query("SELECT * FROM Accounts WHERE username = :username LIMIT 1")
    Account findByUsername(@Param("username") String username);

    @Query("SELECT * FROM Accounts WHERE account_id = :account_id LIMIT 1")
    Account findByAccId(@Param("account_id") int account_id);
}
