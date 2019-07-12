package io.bluemoon.authorizationserver2.service.user;

import io.bluemoon.authorizationserver2.domain.client.OAuthClientDetails;
import io.bluemoon.authorizationserver2.domain.client.OAuthClientDetailsRepository;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUser;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUserRepository;
import io.bluemoon.authorizationserver2.domain.oauth.OAuthUserRoleRepository;
import io.bluemoon.authorizationserver2.utils.APIRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OAuthUserServiceImpl implements OAuthUserService {

    private OAuthUserRepository userRepository;
    private OAuthUserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    private OAuthClientDetailsRepository oAuthClientDetailsRepository;

    public OAuthUserServiceImpl(
            OAuthUserRepository userRepository,
            OAuthUserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            OAuthClientDetailsRepository oAuthClientDetailsRepository
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.oAuthClientDetailsRepository = oAuthClientDetailsRepository;
    }

    @Override
    public OAuthUser createUser(OAuthUser user) {
        // 중복 체크
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public String readUser(OAuthUser user) throws IOException {
        Optional<OAuthUser> optionalOAuthUser =
                userRepository.findByUsername(user.getUsername());

        if (optionalOAuthUser.isPresent()) {

            if (passwordEncoder.matches(user.getPassword(), optionalOAuthUser.get().getPassword())) {
                // client id
                Optional<OAuthClientDetails> optionalOAuthClientDetails = oAuthClientDetailsRepository.findByClientId(user.getUsername());

                if (optionalOAuthClientDetails.isPresent()) {
                    // token 발급
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("grant_type", "password");
                    userInfo.put("username", user.getUsername());
                    userInfo.put("password", user.getPassword());

                    Map<String, Object> authInfo = new HashMap<>();
                    authInfo.put("client_id", optionalOAuthClientDetails.get().getClientId());
                    authInfo.put("client_secret", "1234");

                    APIRequest.ResponseWrapper responseWrapper = APIRequest.getIRequestExecutor().createOAuthToken(userInfo, authInfo);

                    return responseWrapper.getBody();

//                    return optionalOAuthClientDetails.get();
                } else {

                }
//                if (optionalOAuthClientDetails.isPresent()) {
//                    System.out.println(optionalOAuthClientDetails.get());
//                } else {
//                    System.out.println(user.getUsername());
//                    System.out.println(user.getPassword());
//                }

            }
        }

        return null;

    }

    @Override
    public OAuthClientDetails createProject(OAuthClientDetails clientDetails) {
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
        return oAuthClientDetailsRepository.save(clientDetails);
    }
}
