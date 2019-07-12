package io.bluemoon.testservice.service.oauth;

import io.bluemoon.testservice.domain.oauth.OAuthUser;
import io.bluemoon.testservice.domain.oauth.OAuthUserRepository;
import io.bluemoon.testservice.domain.oauth.OAuthUserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthUserServiceImpl implements OAuthUserService {

    private OAuthUserRepository oAuthUserRepository;
    private OAuthUserRoleRepository oAuthUserRoleRepository;

    public OAuthUserServiceImpl(
            OAuthUserRepository oAuthUserRepository,
            OAuthUserRoleRepository oAuthUserRoleRepository
    ) {
        this.oAuthUserRepository = oAuthUserRepository;
        this.oAuthUserRoleRepository = oAuthUserRoleRepository;
    }

}
