package io.bluemoon.authorizationserver2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthController {
    @RequestMapping(value = "/user")
    public Principal getUser(Principal user) {
        return user;
    }

}
