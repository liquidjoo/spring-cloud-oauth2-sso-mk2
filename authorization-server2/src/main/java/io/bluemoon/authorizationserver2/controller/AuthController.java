package io.bluemoon.authorizationserver2.controller;

import io.bluemoon.authorizationserver2.domain.user.User;
import io.bluemoon.authorizationserver2.service.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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

        return user;
    }

    @PutMapping(value = "/updateOAuthUser")
    public User updateOAuthUser(@RequestBody User user, @RequestHeader Map header) {
        System.out.println(user);
        System.out.println(header);

        return user;
    }

}
