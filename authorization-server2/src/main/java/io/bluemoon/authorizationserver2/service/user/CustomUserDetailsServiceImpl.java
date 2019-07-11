package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.user.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    // User Info
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    public CustomUserDetailsServiceImpl(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("why?????????????"+username);
        User user = userRepository.findByUsername(username).get();
        System.out.println(user);
        List<UserRole> userRole = userRoleRepository.findByUser(user);
        System.out.println(userRole);
        System.out.println("---------------------------");
        List<String> urs = new ArrayList<>();
        for (UserRole ur : userRole) {
            urs.add(ur.getRole());
        }

        if (user == null) {
            throw new UsernameNotFoundException("UsernameNotFound[" + username + "]");
        }

        CustomUserDetails userDetail = new CustomUserDetails(user, urs);
        System.out.println(userDetail);
        return userDetail;
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
