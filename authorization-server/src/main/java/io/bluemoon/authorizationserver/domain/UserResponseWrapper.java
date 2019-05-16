package io.bluemoon.authorizationserver.domain;

import lombok.Data;

@Data
public class UserResponseWrapper {

    private boolean result = true;

    private String message;

    private String userName;
}
