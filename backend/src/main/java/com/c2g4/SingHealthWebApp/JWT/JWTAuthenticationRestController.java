package com.c2g4.SingHealthWebApp.JWT;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;

@RestController
@CrossOrigin(origins= "http://localhost:3000")
public class JWTAuthenticationRestController {

    @Value("${com.SHAudit.singHealthAudit.jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService appUserDetailsService;

    @Autowired
    private AccountRepo accountRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping(value = "${com.SHAudit.singHealthAudit.jwt.get.token.uri}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTTokenRequest authenticationRequest)
            throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        logger.warn("AUTHENTICATED");

        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String accountType = accountRepo.getRoleFromUsername(authenticationRequest.getUsername());
        logger.warn(String.format("Token created %s", token));
        return ResponseEntity.ok(new JWTTokenResponse(token,accountType));
    }

    @RequestMapping(value = "${com.SHAudit.singHealthAudit.jwt.refresh.token.uri}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetails user = appUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            String accountType = accountRepo.getRoleFromUsername(user.getUsername());
            return ResponseEntity.ok(new JWTTokenResponse(refreshedToken,accountType));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        logger.debug("in authenticate");

        try {
            logger.debug("into authentication manager");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            logger.debug("done with manager");

        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        }
    }
}