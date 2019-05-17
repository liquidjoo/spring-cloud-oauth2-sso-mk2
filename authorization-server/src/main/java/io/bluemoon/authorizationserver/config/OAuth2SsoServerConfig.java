package io.bluemoon.authorizationserver.config;

import io.bluemoon.authorizationserver.service.user.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
public class OAuth2SsoServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private ApprovalStore approvalStore;
    private ClientDetailsService clientDetailsService;
    private AuthenticationManager authenticationManager;
    private DataSource dataSource;
    private CustomUserDetailsServiceImpl customUserDetailsService;

    public OAuth2SsoServerConfig(
//            AuthorizationCodeServices authorizationCodeServices,
//            ApprovalStore approvalStore,
            ClientDetailsService clientDetailsService,
            AuthenticationManager authenticationManager,
            DataSource dataSource,
            CustomUserDetailsServiceImpl customUserDetailsService
    ) {
//        this.authorizationCodeServices = authorizationCodeServices;
//        this.approvalStore = approvalStore;
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.dataSource = dataSource;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // auth server에 대한 설정
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
//        properties 로 해결 가능
//        super.configure(security);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // OAuth2 서버가 작동하기 위한 Endpoint에 대한 정보를 설정
        endpoints
                // authentication
                .authenticationManager(authenticationManager)
                // jdbc token processing
                .tokenStore(jdbcTokenStore(dataSource))
                // refresh token
                .userDetailsService(customUserDetailsService)
                // approval store
                .approvalStore(approvalStore)
                // code service
                .authorizationCodeServices(authorizationCodeServices);

//                .accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // client 에 대한 설정
        clients.withClientDetails(clientDetailsService);
//        clients.inMemory().withClient("system1").secret("1234")
//                .authorizedGrantTypes("authorization_code", "refresh_token","password")
//                .scopes("read");
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

    @Bean
    public AuthorizationCodeServices jdbcAuthorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public ApprovalStore jdbcApprovalStore(DataSource dataSource) {
        return new JdbcApprovalStore(dataSource);
    }

//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("abc");
////        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray()).getKeyPair("test");
////        converter.setKeyPair(keyPair);
//        return converter;
//    }
}
