package io.bluemoon.authorizationserver2.controller;

import io.bluemoon.authorizationserver2.domain.client.OAuthClientDetails;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUser;
import io.bluemoon.authorizationserver2.service.user.OAuthUserService;
import io.bluemoon.authorizationserver2.utils.APIRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@RestController
public class AuthController {

    private OAuthUserService oAuthUserService;

    public AuthController(
            OAuthUserService oAuthUserService
    ) {
        this.oAuthUserService = oAuthUserService;
    }

    @RequestMapping(value = "/user")
    public Principal getUser(Principal user) {
        return user;
    }


    @PostMapping("/signInMiddleWare")
    public String signInMiddleWare(HttpServletRequest request) throws IOException {
        OAuthUser user = requestToUser(request);

        return oAuthUserService.readUser(user);
    }

    @PostMapping("/signUpMiddleWare")
    public OAuthUser signUpMiddleWare(HttpServletRequest request) {

        OAuthUser user = requestToUser(request);

        return oAuthUserService.createUser(user);

    }

    @PostMapping("/projectCreateMiddleWare")
    public OAuthClientDetails proejctCreateMiddleWare(HttpServletRequest request) {
        OAuthClientDetails authClientDetails = new OAuthClientDetails();
        authClientDetails.setClientId(request.getParameter("client_id"));
        authClientDetails.setClientSecret(request.getParameter("client_secret"));
        return oAuthUserService.createProject(authClientDetails);

    }

    private OAuthUser requestToUser(HttpServletRequest request) {
        OAuthUser user = new OAuthUser();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        return user;
    }




}
