package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class AccountRepoTest {
    @Resource
    private AccountRepo accountRepo;

    private static final int ACCOUNT_ID = 9000;
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    @Test
    public void findByUsername() {
        AccountModel expectedAccount = createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        AccountModel actualModel = accountRepo.findByUsername(USERNAME);
        assert (accountIdentical(expectedAccount,actualModel));
    }

    private boolean accountIdentical(AccountModel accountModel1,AccountModel accountModel2 ){
        boolean acc_id = accountModel1.getAccount_id() == accountModel2.getAccount_id();
        boolean employee_id = accountModel1.getEmployee_id() == accountModel2.getEmployee_id();
        boolean username = accountModel1.getUsername().equals(accountModel2.getUsername());
        boolean password = accountModel1.getPassword().equals(accountModel2.getPassword());
        boolean firstname = accountModel1.getFirst_name().equals(accountModel2.getFirst_name());
        boolean lastname = accountModel1.getLast_name().equals(accountModel2.getLast_name());
        boolean email = accountModel1.getEmail().equals(accountModel2.getEmail());
        boolean hp = accountModel1.getHp().equals(accountModel2.getHp());
        boolean role_id = accountModel1.getRole_id().equals(accountModel2.getRole_id());
        boolean branch_id = accountModel1.getBranch_id().equals(accountModel2.getBranch_id());
        return acc_id && employee_id && username && password && firstname && lastname && email && hp && role_id && branch_id;
    }

    private AccountModel createAccount(){
        AccountModel accountModel = new AccountModel();
        accountModel.setAccount_id(0);
        accountModel.setEmployee_id(0);
        accountModel.setUsername(USERNAME);
        accountModel.setPassword(PASSWORD);
        accountModel.setFirst_name("FIRSTNAME");
        accountModel.setLast_name("LASTNAME");
        accountModel.setEmail("email");
        accountModel.setHp("909");
        accountModel.setRole_id(ResourceString.AUDITOR_ROLE_KEY);
        accountModel.setBranch_id("*");
        return accountModel;
    }

}
