package io.bluemoon.authorizationserver.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;

import java.util.Map;

public class CustomAuthEndpointsApprovalHandler implements UserApprovalHandler {
    @Override
    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        return false;
    }

    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        return null;
    }

    @Override
    public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        return null;
    }

    @Override
    public Map<String, Object> getUserApprovalRequest(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        return null;
    }
}
