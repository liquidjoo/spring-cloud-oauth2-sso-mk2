##Zuul 과 Authorization Server를 통해 SSO 개발 (소셜 로그인 추가)
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

### 간단한 설명


### 시스템 구성도


### 목표


### SSO Login Flow


### Zuul


### Authorization Server


### Keys Points of Sample


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
