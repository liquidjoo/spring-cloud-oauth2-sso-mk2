package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.user.User;

import java.io.IOException;
import java.util.Map;

public interface UserService {

    User createOAuthUser(User user);

    String  createOAuthToken(User user) throws IOException;

    User updateOAuthUser(User user);
}
