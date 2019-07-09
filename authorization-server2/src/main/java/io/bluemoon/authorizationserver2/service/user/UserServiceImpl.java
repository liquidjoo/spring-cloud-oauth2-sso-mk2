package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.user.User;
import io.bluemoon.authorizationserver2.domain.user.UserRepository;
import org.springframework.stereotype.Service;

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
        return userRepository.save(user);
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
}
