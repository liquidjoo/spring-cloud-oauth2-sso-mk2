package io.bluemoon.authorizationserver.controller.sso;

import io.bluemoon.authorizationserver.config.annotation.SocialUser;
import io.bluemoon.authorizationserver.domain.user.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class SsoController {

    private AuthorizationServerTokenServices authorizationServerTokenServices;
    private ConsumerTokenServices consumerTokenServices;

    public SsoController(AuthorizationServerTokenServices authorizationServerTokenServices,
                         ConsumerTokenServices consumerTokenServices) {
        this.authorizationServerTokenServices = authorizationServerTokenServices;
        this.consumerTokenServices = consumerTokenServices;
    }

    @RequestMapping(value = "/user")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping(value = "/login/success")
    public String loginComplete(@SocialUser User user) {
        System.out.println(user);
        // zuul login page redirect
        return "redirect:https://localhost:8765/login";
//        return "why not";
    }


    @PostMapping("/revokeToken")
    public void revokeToken(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
        OAuth2AccessToken accessToken = authorizationServerTokenServices.getAccessToken(oAuth2Authentication);
        consumerTokenServices.revokeToken(accessToken.getValue());
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
    }





//    @GetMapping(value = "/{facebook|google|kakao}/complete")
//    public String loginComplete(HttpSession session) {
//        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
//        Map<String, String> map = (HashMap<String, String>) authentication.getUserAuthentication().getDetails();
//        session.setAttribute("user", User.builder()
//                .username(map.get("username"))
//                .email(map.get("email"))
//                .principal(map.get("id"))
//                .socialType(SocialType.FACEBOOK)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build()
//        );
//        return "redirect:/";
//    }

}
