package com.SHAudit.singHealthAudit.mySQLAccount;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    @Query("select * from Account where user_id = :user_id")
    List<Account> findByUserId(@Param("user_id") int user_id);

}
