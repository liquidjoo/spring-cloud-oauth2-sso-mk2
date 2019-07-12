package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.oauth.OAuthUser;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUserRepository;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUserRole;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUserRoleRepository;
import io.bluemoon.authorizationserver2.domain.user.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    // User Info
    private OAuthUserRepository oAuthUserRepository;
    private OAuthUserRoleRepository oAuthUserRoleRepository;

    public CustomUserDetailsServiceImpl(
            OAuthUserRepository oAuthUserRepository,
            OAuthUserRoleRepository oAuthUserRoleRepository
    ) {
        this.oAuthUserRepository = oAuthUserRepository;
        this.oAuthUserRoleRepository = oAuthUserRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("why?????????????"+username);
        Optional<OAuthUser> user = oAuthUserRepository.findByUsername(username);
        if (user.isPresent()) {
            System.out.println(user);
            List<OAuthUserRole> userRoles = oAuthUserRoleRepository.findByOAuthUser(user.get());
            System.out.println(userRoles);
            System.out.println("---------------------------");
            List<String> urs = new ArrayList<>();
            for (OAuthUserRole ur : userRoles) {
                urs.add(ur.getRole());
            }

            CustomUserDetails userDetail = new CustomUserDetails(user.get(), urs);
            return userDetail;

        } else {
            throw new UsernameNotFoundException("UsernameNotFound[" + username + "]");
        }

    }

    /**
     * User role check
     * @param user
     * @param userRole
     * @return
     */
//    private CustomUserDetails createUser(User user, List<UserRole> userRole) {
//        CustomUserDetails userDetail =
//
////        if (userDetail.getSocial_type().getVaule().equals("FACEBOOK")) {
////            userDetail.setRoles(Arrays.asList("ROLE_FACEBOOK"));
////        } else {
////            userDetail.setRoles(Arrays.asList("ROLE_USER"));
////        }
//        return userDetail;
//    }
}
