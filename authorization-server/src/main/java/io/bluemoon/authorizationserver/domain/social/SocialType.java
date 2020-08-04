package io.bluemoon.authorizationserver.domain.social;

public enum SocialType {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    UNEEDCOMMS("uneedcomms");

    private final String ROLE_PREFIX = "ROLE_";

    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getRoleType() {
        return ROLE_PREFIX + name.toUpperCase();
    }

    public String getVaule() {
        return name;
    }

    public boolean isEquals(String authority) {
        return this.getRoleType().equals(authority);
    }
}
