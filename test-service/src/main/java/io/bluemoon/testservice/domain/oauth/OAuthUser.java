package io.bluemoon.testservice.domain.oauth;

import lombok.Data;

@Data
public class OAuthUser {

    private String username;
    private String password;
    private String name;
    private String email;
    private String ResourceId;
}
