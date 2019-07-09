package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.user.User;

public interface UserService {

    User createOAuthUser(User user);

    User updateOAuthUser(User user);
}
