package io.bluemoon.authorizationserver.config;

import io.bluemoon.authorizationserver.config.handler.CustomAuthFailureHandler;
import io.bluemoon.authorizationserver.service.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomAuthFailureHandler customAuthFailureHandler;

    private CustomUserDetailsService customUserDetailsService;

    public WebSecurityConfig(
            CustomUserDetailsService customUserDetailsService
    ) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * authentication processing
     * if success -> Authentication in info object return
     * els fail -> Exception
     * impl 구현체 -> authentication provider 에서 구현해서 authentication object를 던져줌
     *
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        --------------------------------- sso test
        http.formLogin().loginPage("/login").permitAll().failureHandler(customAuthFailureHandler)
                .and()
                .requestMatchers().antMatchers("/login/**", "/oauth/authorize")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable()
                .and()
                .logout().logoutSuccessUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .oauth2Login()
                .loginPage("/login").permitAll().defaultSuccessUrl("/login/success", true).failureHandler(customAuthFailureHandler);

    }

    /**
     * authentication Object managing
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

//    @Bean
//    @SuppressWarnings("deprecation")
//    public static NoOpPasswordEncoder passwordEncoder() {
//        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//    }

    /**
     * user info at database for make authentication object
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    // social login
//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(
//            OAuth2ClientContextFilter filter
//    ) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new ForwardedHeaderFilter());
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//    private Filter oauth2Filter() {
//        CompositeFilter filter = new CompositeFilter();
//        List<Filter> filters = new ArrayList<>();
//        filters.add(oauth2Filter(facebook(), "/login/facebook", SocialType.FACEBOOK));
//
//        filter.setFilters(filters);
//        return filter;
//    }
//
//    private Filter oauth2Filter(ClientResources client, String path, SocialType socialType) {
//        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
//        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oAuth2ClientContext);
//        filter.setRestTemplate(template);
//        filter.setTokenServices(new UserTokenService(client, socialType));
//        filter.setAuthenticationSuccessHandler((request, response, authentication) ->
//                response.sendRedirect("/" + socialType.getVaule() + "/complete"));
//        filter.setAuthenticationFailureHandler((request, response, exception) ->
//                response.sendRedirect("/error"));
//        return filter;
//    }
//
//    @Bean
//    @ConfigurationProperties("facebook")
//    public ClientResources facebook() {
//        return new ClientResources();
//    }

}
