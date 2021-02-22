package com.SHAudit.singHealthAudit.jwt;

import com.SHAudit.singHealthAudit.mySQLAccount.Account;
import com.SHAudit.singHealthAudit.mySQLAccount.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@EnableJpaRepositories("com.SHAudit.singHealthAudit.mySQLAccount.AccountRepository")
@Service
public class AppUserDetailsService implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<Account> accountList;
        try{
            accountList = accountRepository.findByUserId(Integer.valueOf(s));
        } catch(NumberFormatException e){
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        if(accountList == null || accountList.size() ==0) {
            logger.warn("USERNAME NOT FOUND IN DATABASE");
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        Account account = accountList.get(0);
        logger.warn(String.format("USERNAME %s FOUND IN DATABASE",s));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole_id()));

        UserDetails userDetails = new User(account.getUser_id(), account.getPassword(), authorities);

        return userDetails;
    }
}
