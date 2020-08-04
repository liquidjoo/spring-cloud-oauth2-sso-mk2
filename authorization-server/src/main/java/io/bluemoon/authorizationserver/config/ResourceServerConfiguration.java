package io.bluemoon.authorizationserver.config;


import io.bluemoon.authorizationserver.config.handler.CustomAccessDeniedHandler;
import io.bluemoon.authorizationserver.config.handler.CustomHttp403ForbiddenEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(new CustomHttp403ForbiddenEntryPoint());
        resources.accessDeniedHandler(new CustomAccessDeniedHandler());
        resources.resourceId("resource-id");
    }
}
