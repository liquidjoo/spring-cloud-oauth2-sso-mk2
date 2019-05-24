package io.bluemoon.authorizationserver.controller.sso;

import io.bluemoon.authorizationserver.config.annotation.SocialUser;
import io.bluemoon.authorizationserver.domain.UserResponseWrapper;
import io.bluemoon.authorizationserver.domain.oauth.accesstoken.AccessToken;
import io.bluemoon.authorizationserver.domain.social.SocialType;
import io.bluemoon.authorizationserver.domain.user.User;
import io.bluemoon.authorizationserver.service.sso.SsoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SsoController {

    private SsoService ssoService;

    public SsoController(
            SsoService ssoService
    ) {
        this.ssoService = ssoService;
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    @ResponseBody
    public UserResponseWrapper userResponse(
            @RequestParam(name = "token") String token,
            @RequestParam(name = "clientId") String clientId
    ) {
        AccessToken accessToken = ssoService.getAccessToken(token, clientId);
        UserResponseWrapper userResponseWrapper = new UserResponseWrapper();

        if (accessToken == null) {
            userResponseWrapper.setResult(false);
            userResponseWrapper.setMessage("사용자 정보를 조회할 수 없습니다.");
        } else {
            userResponseWrapper.setMessage(accessToken.getUserName());
        }
        return userResponseWrapper;
    }

    @RequestMapping(value = "/userLogout", method = RequestMethod.GET)
    public String userLogout(
            @RequestParam(name = "clientId") String clientId,
            HttpServletRequest request
    ) {
        String userName = request.getRemoteUser();
        String baseUri = ssoService.logoutAllClient(clientId, userName);

        request.getSession().invalidate();

        return "redirect:"+baseUri;
    }

    @RequestMapping(value = "/oauthCallback", method = RequestMethod.GET)
    public String oauthCallback(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state,
            HttpServletRequest request, ModelMap map
    ) {
        System.out.println(code);
        System.out.println(state);
        System.out.println(request);
        System.out.println(map.toString());

        return "aa";
    }

    @RequestMapping(value = "/user")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping(value = "/loginSuccess")
    @ResponseBody
    public String loginComplete(@SocialUser User user) {
        System.out.println(user);
        return "kkkkkkkkk";
    }

    @GetMapping(value = "/login/success")
    @ResponseBody
    public String test2() {
        return "kkk";
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
