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



/*
 * provider configuration의 중요한 측면은 인증 코드 부여가 OAuth 클라이언트에 제공되는 방식임.
 * 인증 코드는 최종 사용자를 인증 페이지로 안내하여 사용자가 자격 증명을 입력 할 수 있게하여
 * OAuth 클라이언트가 가져 와서 인증 코드가 있는 OAuth 클라이언트로 다시 리디렉션되도록합니다.
 */

@Configuration
@EnableAuthorizationServer
// EnableAuthorizationServer는 OAuth 2.0 인증 서버를 구성 및 사용 설정하고 인증 서버와 상호 작용하는데
// 필요한 여러 엔드 포인트를 사용하도록 설정
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
        // 토큰 엔드포인트의 보안 제한 조건을 정
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
//        properties 로 해결 가능
//        super.configure(security);
    }

    /**
     *  OAuth2 서버가 작동하기 위한 Endpoint에 대한 정보를 설정
     *  권한 부여 및 토큰 엔드 포인트와 토큰 서비스를 설정.
     *  AuhorizationEndpoint가 지원하는 부여 유형을 정할 수 있음.
     * @param endpoints
     * @throws Exception
     */
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
                .reuseRefreshTokens(true)

                // 인증 코드 부여에 대한 인증 코드 서비스
                .authorizationCodeServices(authorizationCodeServices);

//                .accessTokenConverter(jwtAccessTokenConverter());
    }

    /**
     * 클라리언트 세부 사항 서비스의 메모리 내 or JDBC구현을 정의
     * JdbcClientDetailsService를 활용해 데이터베이스 테이블로 세부 정보를 업데이트 가능
     * AuthorizationServerConfigurer의 콜백
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
//        clients.inMemory().withClient("system1").secret("1234")
//                .authorizedGrantTypes("authorization_code", "refresh_token","password")
//                .scopes("read");
    }

    /**
     * 액세스 토큰을 만들 때 액세스 토콘을 수락하는 리소스가 나중에 참조 할 수 있도록 인증을 저장해야함
     * 액세스 토콘을 생성 권한 부여에 사용 된 인증을 로드하는데 사용됨.
     * 서버간에 데이터베이스를 공유 할 수 있는 경우!!
     * @param dataSource
     * @return
     */
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

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("abc");
//        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray()).getKeyPair("test");
//        converter.setKeyPair(keyPair);
        return converter;
    }
}
