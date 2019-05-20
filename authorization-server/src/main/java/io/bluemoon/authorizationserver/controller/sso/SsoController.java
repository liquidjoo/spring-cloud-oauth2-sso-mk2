package io.bluemoon.authorizationserver.controller.sso;

import io.bluemoon.authorizationserver.domain.UserResponseWrapper;
import io.bluemoon.authorizationserver.domain.oauth.accesstoken.AccessToken;
import io.bluemoon.authorizationserver.service.sso.SsoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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

}
