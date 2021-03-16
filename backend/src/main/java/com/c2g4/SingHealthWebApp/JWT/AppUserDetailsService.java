package com.c2g4.SingHealthWebApp.JWT;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;

@Service
public class AppUserDetailsService implements UserDetailsService{
    @Autowired
    private AccountRepo accountRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AccountModel accountModel;
        logger.warn("LOADUSERBYUSERAME");

        accountModel = accountRepo.findByUsername(s);

        if(accountModel == null) {
            logger.warn("USERNAME NOT FOUND IN DATABASE");
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        logger.warn(String.format("USERNAME %s FOUND IN DATABASE",s));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(accountModel.getRole_id()));
        UserDetails userDetails = new User(accountModel.getUsername(), accountModel.getPassword(), authorities);
        return userDetails;
    }
}
