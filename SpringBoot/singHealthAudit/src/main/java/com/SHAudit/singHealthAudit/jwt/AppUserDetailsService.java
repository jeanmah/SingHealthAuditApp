package com.SHAudit.singHealthAudit.jwt;

import com.SHAudit.singHealthAudit.Admin.mySQLAccount.Account;
import com.SHAudit.singHealthAudit.Admin.mySQLAccount.AccountRepository;
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

@EnableJpaRepositories("com.SHAudit.singHealthAudit.Admin.mySQLAccount.AccountRepository")
@Service
public class AppUserDetailsService implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account;
        logger.warn("LOADUSERBYUSERAME");

        try{
            account = accountRepository.findByUsername(s);
        } catch(NumberFormatException e){
            logger.warn("The username {} doesn't exist", s);
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        if(account == null) {
            logger.warn("USERNAME NOT FOUND IN DATABASE");
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        logger.warn(String.format("USERNAME %s FOUND IN DATABASE",s));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole_id()));

        UserDetails userDetails = new User(account.getUsername(), account.getPassword(), authorities);

        return userDetails;
    }
}
