package io.bluemoon.testservice.service.user;

import io.bluemoon.testservice.domain.user.User;
import io.bluemoon.testservice.domain.user.UserRepository;
import io.bluemoon.testservice.utils.APIRequest;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService, ApplicationEventPublisherAware {

    private UserRepository userRepository;
    private ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    @Override
    public User createUser(User user) {
        System.out.println(passwordEncoder().encode(user.getPassword()));
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        System.out.println(passwordEncoder().matches("1234", user.getPassword()));
        userRepository.save(user);
        eventPublisher.publishEvent(new UserCreateEvent(user));
        return user;
    }

    @Override
    public Map readUser(User user) throws IOException {
        System.out.println(user);


        Optional<User> optionalUser =
                userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            System.out.println(optionalUser.get().toString());
            if (passwordEncoder().matches(user.getPassword(),optionalUser.get().getPassword())) {
                APIRequest.ResponseWrapper response = APIRequest.getIRequestExecutor().createOAuthToken(user);
                Map a = new HashMap();
                a.put("data", response);
                return a;
            }

        }

        return null;

    }

    // update password는 별로의 로직으로?
    @Override
    public User updateUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            user.setId(optionalUser.get().getId());
            userRepository.save(user);
            eventPublisher.publishEvent(new UserUpdateEvent(user));
        }
        return user;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    public static class UserCreateEvent {
        @Getter
        private User user;

        private UserCreateEvent(@NonNull User user) {
            this.user = user;
        }
    }

    public static class UserReadEvent {
        @Getter
        private User user;

        private UserReadEvent(@NonNull User user) {
            this.user = user;
        }
    }

    public static class UserUpdateEvent {
        @Getter
        private User user;

        private UserUpdateEvent(@NonNull User user) {
            this.user = user;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
