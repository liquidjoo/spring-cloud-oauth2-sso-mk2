package io.bluemoon.testservice.service.user;

import io.bluemoon.testservice.domain.user.User;

import java.io.IOException;
import java.util.Map;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    Map readUser(User user) throws IOException;
}
