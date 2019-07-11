package io.bluemoon.authorizationserver2.controller;

import io.bluemoon.authorizationserver2.domain.user.User;
import io.bluemoon.authorizationserver2.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
public class AuthController {
    private UserService userService;

    public AuthController(
            UserService userService
    ) {
        this.userService = userService;
    }


    @RequestMapping(value = "/user")
    public Principal getUser(Principal user) {
        return user;
    }


    @RequestMapping(value = "/createOAuthUser", method = RequestMethod.POST)
    public User createOAuthUser(@RequestBody @NotNull User user, @RequestHeader Map header, Errors errors) {
        System.out.println(errors.toString());
        System.out.println(user);
        System.out.println(header);

        return userService.createOAuthUser(user);
    }

    @RequestMapping(value = "/createToken", method = RequestMethod.POST)
    public String createToken(@RequestBody User user, @RequestHeader Map header) throws IOException {
        return userService.createOAuthToken(user);
    }

    @PutMapping(value = "/updateOAuthUser")
    public User updateOAuthUser(@RequestBody User user, @RequestHeader Map header) {
        System.out.println(user);
        System.out.println(header);

        return user;
    }



}
