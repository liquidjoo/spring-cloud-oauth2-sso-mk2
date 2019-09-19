package io.bluemoon.authorizationserver.config.handler;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = CustomOAuthExceptionSerializer.class)
public class CustomOAuthException extends OAuth2Exception {
    public CustomOAuthException(String msg) {
        super(msg);
    }
}
