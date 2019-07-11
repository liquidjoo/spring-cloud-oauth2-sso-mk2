package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.user.User;
import io.bluemoon.authorizationserver2.domain.user.UserRepository;
import io.bluemoon.authorizationserver2.utils.APIRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    @Override
    public User createOAuthUser(User user) {
//        user.setPassword(passwordEncoder().encode(user.getPassword()));
//        user.setPassword();
        return userRepository.save(user);
    }

    @Override
    public String createOAuthToken(User user) throws IOException {

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        System.out.println(passwordEncoder().matches(user.getPassword(), optionalUser.get().getPassword()));
//        System.out.println(passwordEncoder().matches("1234", optionalUser.get().getPassword()));
        if (optionalUser.isPresent()) {
            if (passwordEncoder().matches(user.getPassword(), optionalUser.get().getPassword())) {
                //token 발급
                Map<String, Object> tokenInfo = new HashMap<>();
                tokenInfo.put("username", user.getUsername());
                tokenInfo.put("password", user.getPassword());
//                tokenInfo.put("password", "1234");
                tokenInfo.put("grant_type", "password");
                APIRequest.ResponseWrapper response = APIRequest.getIRequestExecutor().createOAuthToken(tokenInfo);
                System.out.println(response.getBody());
                return response.getBody();

            }
            System.out.println("-----------------------11");
        }
        System.out.println("-----------------------22");
        return null;
    }

    @Override
    public User updateOAuthUser(User user) {
        Optional<User> getUser = userRepository.findById(user.getId());
        if (getUser.isPresent()) {
            user.setId(getUser.get().getId());
            return userRepository.save(user);

        } else {
            return null;
        }
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    @SuppressWarnings("deprecation")
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
