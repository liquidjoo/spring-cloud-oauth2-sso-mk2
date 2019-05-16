package io.bluemoon.authorizationserver.service.sso;

import io.bluemoon.authorizationserver.domain.oauth.accesstoken.AccessToken;


public interface SsoService {

    AccessToken getAccessToken(String token, String clientId);

    String logoutAllClient(String clientId, String userName);

}
