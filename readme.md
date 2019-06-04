
# (작성중)
## Zuul 과 Authorization Server를 통해 SSO 개발 (소셜 로그인 추가)
- 시스템 구성도
- 어떠한 기능들이 쓰였나 (os, 기술, dependency, etc..)
- 디테일한 설명
- 고칠곳..?
- 참고 자료들

- overview
- usage
- gradle
- Goals
- keys points of sample

### 설명
스프링 클라우드를 사용해서 만든 OAuth2 SSO 시스템 개발. 

### 시스템 구성도


### 목표
가장 큰 목표는 SSO Login을 Zuul을 통해 AuthorizationServer에 대한 정보를 가리기 위함인데
아마 ```security.oauth2.client.user-authorization-uri```, ```security.oauth2.client.access-token-uri```
이 두부분에 uri에 url을 넣어서 그런것 같은데 설정이 잘 못되었는지 서비스 디스커버리를 쓰지 않아서인지 uri만 입력시에 잘못된 uri라고 뜨더군요..
혹시 알고 계신다면 pr을 주시면 수정하여 반영하겠습니다. 그 전에 고쳐진다면 수정해서 반영해놓겠습니다~!.
또 많은 예가 있었지만 AuthorizationServerConfigurerAdapter 의 config 설정이 in-memory로 되어 있어서 여러 유저가 존재할 때
mysql 기준으로 데이터베이스를 통해 인증을 할 수 있게 만들었습니다.


### SSO Login Flow


### Zuul


### Authorization Server


### Keys Points of Sample


### 후기
서비스 디스커버리 (유레카)를 사용하지 않아서 조금의 차이가 있습니다... 서비스 디스커버리를 사용하게되면
개발 리소스(유지 및 보수) 가 추가되어서 빼고 Zuul에서 URL을 통해 라우트를 처리했습니다.
많은분들의 문서 및 레포지토리를 참고하여 만들었습니다. 가장 많은 도움이 되었던 레포지토리 (https://github.com/kakawait/uaa-behind-zuul-sample)


### Ref



SSO

Zuul gateway
- sso Login path 
    - application.properties (security.oauth2.sso.login-path = /login)
    - 위의 sso login path 프로퍼티 설정은 Authorization Server 의 login page로 리다이렉트 해준다.
    
- filter
    - csrf RequestMatcher

@EnableOAuth2Sso
@ResourceServer
WebSecurityConfigurerAdapter
- HttpSecurity http setting


Authorization Server
