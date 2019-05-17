package io.bluemoon.testservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@EnableResourceServer
@SpringBootApplication
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
            return principal == null ? "hello anonymous" : "heelo" + principal.getName();
        }

        @PreAuthorize("#oauth2.hasScope('read') and hasRole('ROLE_USER')")
        @RequestMapping(value = "secret", method = RequestMethod.GET)
        @ResponseBody
        public String helloMk2Secret(Principal principal) {
            return principal == null ? "hello anonymous" : "heelo" + principal.getName();
        }

        @RequestMapping(method = RequestMethod.GET, value = "test")
        @ResponseBody
        public String test() {
            return "test";
        }
    }

}
