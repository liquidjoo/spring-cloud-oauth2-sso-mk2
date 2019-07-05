package io.bluemoon.zuuloauth2;

import io.bluemoon.zuuloauth2.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy
public class ZuulOauth2Application {

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
//        RestTemplate template = new RestTemplate();
//        List interceptors = template.getInterceptors();
//        if (interceptors == null) {
//            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
//        } else {
//            interceptors.add(new UserContextInterceptor());
//            template.setInterceptors(interceptors);
//        }
//        return template;
        return new RestTemplate();
    }

    @Controller
    @RequestMapping("/")
    public static class TestController {
        @RequestMapping(method = RequestMethod.GET)
        public String test(Principal principal) {
            System.out.println(principal.getName());
            System.out.println(principal.toString());
            return "aa";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ZuulOauth2Application.class, args);
    }

}
