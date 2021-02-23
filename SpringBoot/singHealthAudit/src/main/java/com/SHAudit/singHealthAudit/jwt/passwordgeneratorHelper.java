package com.SHAudit.singHealthAudit.jwt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class passwordgeneratorHelper {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPasrd = "testing12ddfgfdsg3";
        int t = 234;
        String encodedPassword = encoder.encode(rawPasrd);

        System.out.println(encodedPassword); //$2a$10$4h6b0xhD2A64FgQ8B2CeaOSLI3ZpLexh5fyEaEPt9tyI33V5pkdim
    }

}
