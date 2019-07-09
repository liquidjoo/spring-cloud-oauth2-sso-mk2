package io.bluemoon.testservice.controller;

import io.bluemoon.testservice.domain.user.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class SignController {
    @PostMapping("/signInAfter")
    private String signIn(HttpServletRequest request) {
        System.out.println(request);
        System.out.println(request.getAuthType());
        System.out.println(request.getSession());
        System.out.println(request.getParameterMap().toString());

        return "aaaa";
    }

    @PostMapping("/createOAuthUser")
    public String creatOAuthUser(@RequestBody @Valid User user, @RequestHeader Map header) {



        return "";
    }
}
