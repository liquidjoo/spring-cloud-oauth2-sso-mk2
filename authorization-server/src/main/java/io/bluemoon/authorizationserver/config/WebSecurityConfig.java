package io.bluemoon.authorizationserver.config;

import io.bluemoon.authorizationserver.service.user.CustomUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.BASIC_AUTH_ORDER - 6)
@Order(-1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomUserDetailsServiceImpl customUserDetailsService;

    public WebSecurityConfig(
            CustomUserDetailsServiceImpl customUserDetailsService
    ) {
        this.customUserDetailsService = customUserDetailsService;
    }
    /**
     * authentication processing
     * if success -> Authentication in info object return
     * els fail -> Exception
     * impl 구현체 -> authentication provider 에서 구현해서 authentication object를 던져줌
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
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
            .authorizeRequests()
                .antMatchers("/", "/login/**", "/css/**", "/images/**", "/js/**",
                        "/console/**", "/oauth2/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .oauth2Login()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
            .and()
                .headers().frameOptions().disable()
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and()
                .formLogin().loginPage("/login")
            .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSSIONID")
                .invalidateHttpSession(true)
            .and()
                .addFilterBefore(filter, CsrfFilter.class);
//                .csrf().disable();
//        http.formLogin().loginPage("/login").permitAll()
//                .and()
//                .requestMatchers().antMatchers("/login", "/logout", "/oauth/authorize", "/oauth/confirm_access")
//                .and()
//                .authorizeRequests().anyRequest().authenticated();

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

    @Bean
    @SuppressWarnings("deprecation")
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
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
