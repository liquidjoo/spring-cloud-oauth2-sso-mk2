package io.bluemoon.authorizationserver2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class AuthorizationServer2Application {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServer2Application.class, args);
    }

}
