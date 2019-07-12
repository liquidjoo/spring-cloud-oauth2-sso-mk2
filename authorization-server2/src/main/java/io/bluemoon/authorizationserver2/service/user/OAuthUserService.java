package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.client.OAuthClientDetails;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUser;
import io.bluemoon.authorizationserver2.utils.APIRequest;

import java.io.IOException;

public interface OAuthUserService {
    OAuthUser createUser(OAuthUser user);

    String readUser(OAuthUser user) throws IOException;

    OAuthClientDetails createProject(OAuthClientDetails clientDetails);

}
