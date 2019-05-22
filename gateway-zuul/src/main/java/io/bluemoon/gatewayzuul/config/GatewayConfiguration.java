package io.bluemoon.gatewayzuul.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 리소스 서버는 OAuth2 토큰에 의해 보호되는 리소스를 제공.
 * Spring OAuth2는 이 보호 기능을 구현하는 Spring security 인증 필터를 제공.
 */
//@Configuration
//@EnableResourceServer
//@EnableOAuth2Sso
//@Order(value = -1)
public class GatewayConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/mk-auth/**", "login").permitAll()
                .anyRequest()
                .authenticated();
    }
}
