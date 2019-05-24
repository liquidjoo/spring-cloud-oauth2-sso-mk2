package io.bluemoon.authorizationserver.service.user;

import io.bluemoon.authorizationserver.domain.user.User;
import io.bluemoon.authorizationserver.domain.user.UserDetail;
import io.bluemoon.authorizationserver.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    // User Info
    private UserRepository userRepository;

    public CustomUserDetailsServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        System.out.println(user);

        if (user == null) {
            throw new UsernameNotFoundException("UsernameNotFound[" + username + "]");
        }

        UserDetail userDetail = createUser(user);
        System.out.println(userDetail);
        return userDetail;
    }

    /**
     * User role check
     * @param user
     * @return
     */
    private UserDetail createUser(User user) {
        UserDetail userDetail = new UserDetail(user);
        userDetail.setRoles(Arrays.asList("ROLE_USER"));

//        if (userDetail.getSocial_type().getVaule().equals("FACEBOOK")) {
//            userDetail.setRoles(Arrays.asList("ROLE_FACEBOOK"));
//        } else {
//            userDetail.setRoles(Arrays.asList("ROLE_USER"));
//        }
        return userDetail;
    }
}
