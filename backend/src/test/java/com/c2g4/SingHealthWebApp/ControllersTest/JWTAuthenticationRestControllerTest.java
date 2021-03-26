package com.c2g4.SingHealthWebApp.ControllersTest;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class JWTAuthenticationRestControllerTest {
    //TODO: test username, password
    //valid user, valid password
    //valid user, invalid password
    //invalid user
    //TODO: test have username, no password
    //no username have password
    //no username, no password
    //something about the tokens
}
