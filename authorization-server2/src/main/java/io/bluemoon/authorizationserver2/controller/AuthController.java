package io.bluemoon.authorizationserver2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
