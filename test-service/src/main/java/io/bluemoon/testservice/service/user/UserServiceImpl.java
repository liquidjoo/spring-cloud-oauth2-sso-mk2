package io.bluemoon.testservice.service.user;

import io.bluemoon.testservice.domain.user.User;
import io.bluemoon.testservice.domain.user.UserRepository;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
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

        userRepository.save(user);
        return null;
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
}
