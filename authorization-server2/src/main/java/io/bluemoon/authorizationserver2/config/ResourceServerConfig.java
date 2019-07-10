package io.bluemoon.authorizationserver2.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
//@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                .requestMatchers().antMatchers("/createOAuthUser")
//                .and()
                .authorizeRequests()
                .antMatchers("/createOAuthUser").permitAll()
                .anyRequest()
                .authenticated();
    }
}
