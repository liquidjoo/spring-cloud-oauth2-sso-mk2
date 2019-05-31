package io.bluemoon.authorizationserver.domain.social;

import io.bluemoon.authorizationserver.config.annotation.SocialUser;
import io.bluemoon.authorizationserver.domain.user.User;
import io.bluemoon.authorizationserver.domain.user.UserRepository;
import io.bluemoon.authorizationserver.domain.user.UserRole;
import io.bluemoon.authorizationserver.domain.user.UserRoleRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    public UserArgumentResolver(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SocialUser.class) != null &&
                parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");
        return getUser(user, session);
    }

    /**
     * 인증된 User 객체를 만드는 메인 메서드
     * @param user
     * @param session
     * @return
     */
    private User getUser(User user, HttpSession session) {
        // 세션에서 가져온 유저가 널일 경우에만
        System.out.println("-------------------------------------");
        System.out.println(user);
        if (user == null) {
            try {
//                OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
//                Map<String, String> map = (HashMap<String, String>) authentication.getUserAuthentication().getDetails();
//                User convertUser = convertUser(String.valueOf(authentication.getAuthorities().toArray()[0]), map);
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                // SecurityContext 사용자의 보호 및 인증된 세션
                Map<String, Object> map = authentication.getPrincipal().getAttributes();
                System.out.println(map.toString());
                User convertUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map);
                System.out.println(convertUser.toString());

                user = userRepository.findByEmail(convertUser.getEmail());
                if (user == null) {
                    user = userRepository.save(convertUser);
                    UserRole userRole = UserRole.builder()
                            .role("USER")
                            .user(user)
                            .build();
                    UserRole userRoles = userRoleRepository.save(userRole);

                }

                List<UserRole> userRoles = userRoleRepository.findByUser(user);
                // role 부여

               setRoleIfNotSame(user, authentication, map, userRoles);
                session.setAttribute("user", user);
            } catch (ClassCastException e) {
                return user;
            }
        }
        System.out.println(user);
        return user;
    }

    /**
     * 사용자의 인증된 소셜 미디어 타입에 따라 빌더를 사용하여 User 객체를 만들어 주는 가교 역할
     * @param authority
     * @param map
     * @return
     */
    private User convertUser(String authority, Map<String, Object> map) {
        if (SocialType.FACEBOOK.getVaule().equals(authority)) return getModernUser(SocialType.FACEBOOK, map);
        else if (SocialType.GOOGLE.getVaule().equals(authority)) return getModernUser(SocialType.GOOGLE, map);

        return null;
    }

    /**
     * 페이스북이나 구글 같이 공통되는 명명규칙을 가진 그룹을 맵핑
     * @param socialType
     * @param map
     * @return
     */
    private User getModernUser(SocialType socialType, Map<String, Object> map) {

        if (socialType.getVaule().equals("facebook")) {
            return User.builder()
                    .name(String.valueOf(map.get("name")))
                    .email(String.valueOf(map.get("email")))
                    .principal(String.valueOf(map.get("id")))
                    .socialType(socialType)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else if (socialType.getVaule().equals("google")) {
            return User.builder()
                    .name(String.valueOf(map.get("name")))
                    .email(String.valueOf(map.get("email")))
                    .principal(String.valueOf(map.get("sub")))
                    .socialType(socialType)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            return User.builder()
                    .name(String.valueOf(map.get("name")))
                    .email(String.valueOf(map.get("email")))
                    .principal(String.valueOf(map.get("id")))
                    .socialType(socialType)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 인증된 authentication이 권한을 갖고 있는지 체크하는 용도
     * 만약 저장된 User 권한이 없으면 SecurityContextHolder를 사용하여 해당 소셜미디어 타입으로 권한을 저장
     * @param user
     * @param authentication
     * @param map
     */
    private void setRoleIfNotSame(User user, OAuth2AuthenticationToken authentication, Map<String, Object> map, List<UserRole> userRoles) {
        Map<String, Object> principalMap = new HashMap<>();
        if (user.getSocialType().getVaule().equals("google")) {
            principalMap.put("id", map.get("sub"));
            principalMap.put("name", map.get("name"));
            principalMap.put("email", map.get("email"));
        } else {
            principalMap = map;
        }

        // spring security authentiaction params setting
        // 후.. 찾기 힘들었다..
//        if (!authentication.getAuthorities().contains(
//                new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
//            SecurityContextHolder.getContext().setAuthentication(
//                    new UsernamePasswordAuthenticationToken(principalMap, "N/A", AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType()))
//            );
//        }

        // social default user role
        if (userRoles != null) {
            List<GrantedAuthority> authoritiesRole = new ArrayList<>(userRoles.size());
            for (UserRole ur : userRoles) {
                authoritiesRole.add(new SimpleGrantedAuthority(ur.getRole()));
            }

            if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(principalMap, "N/A", authoritiesRole)
                );
            }
        } else {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principalMap, "N/A", AuthorityUtils.createAuthorityList("ROLE_NONE")));
        }

    }


}
