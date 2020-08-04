package io.bluemoon.testservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@SpringBootApplication
@EnableResourceServer
public class TestServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
    }

    @Controller
    @RequestMapping("/")
    public static class TestController{

        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
        public String helloMk2(Principal principal) {

            System.out.println("-------------");
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authentication;
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();

//            System.out.println(userDetails.getUsername());
            System.out.println(oAuth2AuthenticationDetails.getTokenValue());
            System.out.println("-------------");
            return principal == null ? "hello anonymous" : "heelo" + principal.getName();
        }


        @RequestMapping(method = RequestMethod.GET, value = "/tts")
        @ResponseBody
        public void helloMk3(Principal principal) {
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SecurityContextHolder.clearContext();
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

            SecurityContextHolder securityContextHolder = (SecurityContextHolder) SecurityContextHolder.createEmptyContext();
            System.out.println(securityContextHolder);

        }


        @PreAuthorize("#oauth2.hasScope('read') and hasRole('ROLE_USER')")
        @RequestMapping(value = "secret", method = RequestMethod.GET)
        @ResponseBody
        public String helloMk2Secret(Principal principal) {
            return principal == null ? "hello anonymous" : "heelo" + principal.getName();
        }
        
    }

}
