package io.bluemoon.gatewayzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableZuulProxy
@SpringBootApplication
public class GatewayZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayZuulApplication.class, args);
    }

    @Controller
    public static class TestController {

        @RequestMapping(value = "/gateway/logout", method = RequestMethod.GET)
        public String signOut(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            Object details = authentication.getDetails();
            String token = ((OAuth2AuthenticationDetails) details).getTokenValue();
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/mk-auth/revokeToken";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);


            HttpSession httpSession = request.getSession();
            httpSession.invalidate();
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                cookie.setPath("/");
                cookie.setSecure(true);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

            return "redirect:" + "http://localhost:8765/mk-auth/rending";
        }
    }

//    @Bean
//    UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer(LoadBalancerInterceptor loadBalancerInterceptor) {
//        return template -> {
//            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//            interceptors.add(loadBalancerInterceptor);
//            AccessTokenProviderChain accessTokenProviderChain = Stream
//                    .of(new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
//                            new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider())
//                    .peek(tp -> tp.setInterceptors(interceptors))
//                    .collect(Collectors.collectingAndThen(Collectors.toList(), AccessTokenProviderChain::new));
//            template.setAccessTokenProvider(accessTokenProviderChain);
//        };
//    }

}
