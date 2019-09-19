package io.bluemoon.authorizationserver.config.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class CustomResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        if (e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new CustomOAuthException(oAuth2Exception.getMessage()));
        } else if (e instanceof AuthenticationException) {
            AuthenticationException authenticationException = (AuthenticationException) e;
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CustomOAuthException(authenticationException.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomOAuthException(e.getMessage()));
    }
}
