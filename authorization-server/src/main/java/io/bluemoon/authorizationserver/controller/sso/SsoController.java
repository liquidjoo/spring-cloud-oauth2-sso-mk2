package io.bluemoon.authorizationserver.controller.sso;

import io.bluemoon.authorizationserver.config.annotation.SocialUser;
import io.bluemoon.authorizationserver.domain.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class SsoController {

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
