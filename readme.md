
# Zuul 과 Authorization Server를 통해 SSO 개발 (소셜 로그인 추가)
- overview
- usage
- gradle
- Goals
- keys points of sample

## 설명 *() 괄호 안의 내용은 프로젝트 이름*
- 스프링 클라우드를 사용해서 만든 OAuth2 SSO 시스템 개발. *(gateway-zuul, Authorization-server)*

## 환경
- java 8
- Spring boot 2.1.5
- Gradle


## 목표
가장 큰 목표는 SSO Login을 Zuul을 통해 AuthorizationServer에 대한 정보를 가리기 위함인데 아마 ```security.oauth2.client.user-authorization-uri```, ```security.oauth2.client.access-token-uri```   이 두부분에 uri에 url을 넣어서 그런것 같은데 설정이 잘 못되었는지 서비스 디스커버리를 쓰지 않아서인지 uri만 입력시에 잘못된 uri라고 뜨더군요..  
혹시 알고 계신다면 pr을 주시면 수정하여 반영하겠습니다. 그 전에 고쳐진다면 수정해서 반영해놓겠습니다~!.  
또 많은 예가 있었지만 AuthorizationServerConfigurerAdapter 의 config 설정이 in-memory로 되어 있어서 여러 유저가 존재할 때 mysql 기준으로 데이터베이스를 통해 인증을 할 수 있게 만들었습니다. 

## 인증 네트워크
Zuul에 등록되어진 리소스 서비스에 접근하려고 할 때 인증 및 인가 네트워크
![Image of before_auth](https://github.com/liquidjoo/spring-cloud-oauth2-sso-mk2/blob/master/request_auth_network.png)

로그인이 성공했을 때
![Image of after_auth](https://github.com/liquidjoo/spring-cloud-oauth2-sso-mk2/blob/master/after_login_network.png)

## SSO Login Flow
![Image of flow](https://github.com/liquidjoo/spring-cloud-oauth2-sso-mk2/blob/master/zuul_flow.png)  
```ref) https://github.com/kakawait/uaa-behind-zuul-sample ```  

## Zuul
**gateway-zuul(SSO Gateway)**
security 내의 **security.oauth2.sso.login-path=/login** 프로퍼티 설정과 zuul의 게이트웨이 특성을 이용해 sso로 구현이 가능.
zuul이 zuul proxy server, resource server 역할을 수행.  
    
```java
    @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/", "/mk-auth/**", "/login").permitAll().anyRequest().authenticated()
                    .and()
            .logout().logoutSuccessUrl("/gateway/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true).deleteCookies("JSESSIONID").clearAuthentication(true);
    
        }
```
antMatchers에 접근하고자 하는 uri 접근제어.  

**logout**
logout 요청시 (http://localhost:8765/logout) /gateway/logout을 통해 zuul 쿠키 및 세션을 삭제, token revoke 후  
redirect를 통해 인증 서버에서도 쿠키 및 세션을 삭제  


**주요 properties 설정**
```
security.oauth2.sso.login-path=/login
security.oauth2.client.access-token-uri=http://localhost:8765/mk-auth/oauth/token
# /oauth/authorize 요청은 클라이언트가 리소스 서버의 api를 사용하기 위해 사용자(리소스 소유자)에게
# 권한 위임 동의를 받기 위한 페이지를 출력하는 기능을 수행
security.oauth2.client.user-authorization-uri=http://localhost:8765/mk-auth/oauth/authorize
security.oauth2.resource.user-info-uri=http://localhost:8081/mk-auth/user
```  
sso.login-path를 통해 인증서버와 통신 후 user-info-uri를 통해 인증이 데이터가 정상적으로 나오면 security, zuul.routes를 통해 sso처럼 접근이 가능.

## Authorization Server (User Account and Authentication Service -> UAA)

**권한 코드 방식(Authorization Code flow)** *[current project name = authorization-server]*    
클라이언트가 다른 사용자 대신 특정 리소스에 접근을 요청할 때 사용되어짐.  
리소스 접근을 위한 id, password, code(auth server)를 사용해서 리소스에 대한 엑세스 토큰 발급.  
현재 SSO Login 시에 사용된 인증 방식으로 구현.  
gateway-zuul 프로젝트 내에서 ```security.oauth2.sso.login-path=/login``` 의 프로퍼티를 사용해서 login page로 이동 시켜준다.  
물론 이 때 로그인패스는 UAA(auth server)의 로그인 페이지로 이동하며 properties(gateway-zuul)내의 client-id, client-secret, redirect_url 을 사용해 redirect_uri로 code를 발급 후
id, password를 받기 위해 login page로 이동하게 되어진다.
두 단계로 나누어서 설명 (위의 SSO Login Flow를 보게 되면 과정을 알 수가 있다.)
1. 코드 발급
```
curl -X GET http://localhost/oauth/authorize -G -d "client_id=system" -d "scope=read" -d "grant_type=authorization_code" -d "response_type=code" -d "redirect_uri=http://localhost/login"
```
2. 발급된 코드로 인증
인증이 완료가 되었으면 redirect_uri로 query_string이 code=asdf 이런 식으로 붙어서 오게 되며
``` 
curl -u client_id:client_secret http://localhost/oauth/token -d "grant_type=authorization_code" -d "code=asdf" -d "scope=read" -d "redirect_uri=http://localhost/login" -d "username=blue" -d "password=moon"
```
* 문제점 sso login form이 있는데 curl 을 통해 토큰을 발급하게 되면 로그인 페이지로 계속 리다이렉트 되어서 인증 토큰이 정상적으로 발급이 잘 안됨..(실력 부족 ㅜ)


**리소스 소유자 암호 자격 증명 타입(Resource Owner Password Credentials Grant Type)** *[current project name = authorization-server2]*  
리소스 접근 시에 id, password, client-id, client-secret 사용해서 리소스에 대한 엑세스 토큰, 리프레쉬 토큰 발급
```
curl -u client_id:client_secret http://localhost/oauth/token -d "grant_type=password&username=blue&password=moon"
```

리프레쉬 토큰도 같이 발급되며 리프레쉬 토큰이 만료 되기 전까진 토큰을 생성하는게 아니라 리프레쉬 토큰으로 액세스 토큰을 갱신 (2ddb0b27-a62a-4719-b0c4-c8b23ab537bd <- 발급 받은 토큰)
```
curl -u client_id:client_secret http://localhost/oauth/token -d "grant_type=refresh_token&scope=read&refresh_token=2ddb0b27-a62a-4719-b0c4-c8b23ab537bd"
```


## 토큰 발급 후
- OAuth Token 사용 시
 Cookie에 http only 속성을 추가 후에 토큰(암호화(복호화 가능하게))을 담는다 (access-token)
 refresh-token은 별도의 storage가 필요 (보안을 위해)
 Spring Security Context Holder, Principal 객체를 통해 토큰에 대한 유저 정보를 받아 볼 수 있다. 
- JWT 사용 시 
 각 서비스 별로 JWT 해석기가 필요하며, JWT를 사용 시에는 인증서를 통해 jwt를 만들면 된다.
 jwt는 이미 토큰에 정보를 갖고 있기에 db에 대한 레이턴시가 OAuth token에 비해 많이 줄어든다. 각 유저 정보와 jwt 토큰 관리하는 스토리지(redis)가 존재하면 된다.
  
- refresh token
 1. 로그인 성공 시에 Access Token, Refresh Token을 발급.  
 2. 회원 관리 스토리지에 Refresh Token은 따로 저장 후 Access Token만 헤더에 실어서 요청을 보냄.  
 3. Validation Access Token (check_token uri 이용)
 4. 각자의 로직이 들어감 예) Access Token이 만료 되었으면 갱신을 할 것인 가 또는 다시 로그인을 시킬 것인가 등의 작업이 필요.
 
 
 ## zuul dynamic routing
 개발 하면서 zuul에서 라우팅할 때 포워딩, 라우팅 되어 있는 서비스들의 헬스 체크 같은걸 함.  
 라우팅 되어 있는 서비스가 끊겼을 때 zuul에서는 커넥션 에러와 포워딩 익셉션을 던지며, 서비스가 켜져 있더라고 하더라도  
 이미 끊겼다면 동적 라우팅 처리를 해줘야한다.    
 ```bash
com.netflix.zuul.exception.ZuulException: Forwarding error
at org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter.handleException(SimpleHostRoutingFilter.java:257) ~[spring-cloud-netflix-zuul-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
at org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter.run(SimpleHostRoutingFilter.java:237) ~[spring-cloud-netflix-zuul-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
...

connection refuse ~
```

동적라우팅 적용 예정  

## zuul filter
```java
    @Override
    public boolean shouldFilter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ...
        if (auth instanceof OAuth2Authentication) {
            ...
            return true;
        }
        return false;
    }
```  
shouldFilter를 통해 인증을 먼저 체크
run() 메소드에서 민감한 uri, url 리다이렉트  

## Keys Points of Sample

## 후기
서비스 디스커버리 (유레카)를 사용하지 않아서 조금의 차이가 있습니다... 서비스 디스커버리를 사용하게되면 개발 리소스(유지 및 보수) 가 추가되어서 빼고 Zuul에서 URL을 통해 라우트를 처리했습니다.  
많은분들의 문서 및 레포지토리를 참고하여 만들었습니다.

## 새로운 작업
DDD 적용예정
적용 예정 project [authorization server, authorization server2, test-service]

할 수 있는 부분은 자바8 사용 예정

## Ref
```
https://github.com/kakawait/uaa-behind-zuul-sample
https://github.com/keets2012/microservice-integration
https://github.com/artemMartynenko/spring-cloud-gateway-oauth2-sso-sample-application
https://github.com/Baeldung/oauth-microservices
https://cheese10yun.github.io/oauth2
```
