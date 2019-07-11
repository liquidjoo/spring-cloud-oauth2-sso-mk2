package io.bluemoon.testservice.controller;

import io.bluemoon.testservice.domain.user.User;
import io.bluemoon.testservice.service.user.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
public class SignController {

    private UserService userService;

    public SignController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/signInMiddleWare")
    public Map signInMiddleWare(HttpServletRequest request) throws IOException {
        User user = requestToUser(request);

        return userService.readUser(user);
    }

    @PostMapping("/signUpMiddleWare")
    public User signUpMiddleWare(HttpServletRequest request) {

        User user = requestToUser(request);

        return userService.createUser(user);

    }

    @PostMapping("/createOAuthUser")
    public String creatOAuthUser(@RequestBody @Valid User user, @RequestHeader Map header) {

        return "";
    }


    private User requestToUser(HttpServletRequest request) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));


        return user;
    }

}
