package com.SHAudit.singHealthAudit.otherutilityclasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class passwordgeneratorHelper {

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptString(String toEncrypt){
        return encoder.encode(toEncrypt);
    }

    private static void generatePassword(String rawPassword){
        String encodedPassword = encryptString(rawPassword);
        System.out.println(encodedPassword);
    }

    public static void main(String[] args) {
        generatePassword("test123"); //$2a$10$ekk.tCk.RCkXMLaak1hu3ur4qhj6.21KhkxX7cQve3mg.4/9onvvu
    }

}
