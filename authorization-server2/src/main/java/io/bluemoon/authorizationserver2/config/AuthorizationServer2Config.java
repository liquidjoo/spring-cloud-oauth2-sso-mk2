package io.bluemoon.authorizationserver2.config;

import io.bluemoon.authorizationserver2.service.user.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ApprovalStore approvalStore;

    private ClientDetailsService clientDetailsService;
    private AuthenticationManager authenticationManager;
    private DataSource dataSource;
    private CustomUserDetailsServiceImpl customUserDetailsService;

    public AuthorizationServer2Config(
            ClientDetailsService clientDetailsService,
            AuthenticationManager authenticationManager,
            DataSource dataSource,
            CustomUserDetailsServiceImpl customUserDetailsService
    ) {
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.dataSource = dataSource;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // authentication
                // 비밀 번호 부여는 AuthenticationManager를 주입해야 켜짐
                .authenticationManager(authenticationManager)
                // jdbc token processing
                .tokenStore(jdbcTokenStore(dataSource))

                // 사용자 세부 정보가 필요할 때
                .userDetailsService(customUserDetailsService)
                // approval store
                .approvalStore(approvalStore)
                // refresh token
                .reuseRefreshTokens(true);

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Bean
    public TokenStore jdbcTokenStore(DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    @Primary
    public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
        return new JdbcClientDetailsService(dataSource);
    }

//    @Bean
//    public AuthorizationCodeServices jdbcAuthorizationCodeServies(DataSource dataSource) {
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }

    @Bean
    public ApprovalStore jdbcApprovalStore(DataSource dataSource) {
        return new JdbcApprovalStore(dataSource);
    }
    // jwt



}
