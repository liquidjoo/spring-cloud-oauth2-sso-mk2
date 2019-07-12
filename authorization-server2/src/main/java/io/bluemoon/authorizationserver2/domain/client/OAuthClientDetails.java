package io.bluemoon.authorizationserver2.domain.client;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "oauth_client_details")
public class OAuthClientDetails {

    @Id
    @Column
    private String clientId;

    @Column
    private String resourceIds;

    @Column
    private String clientSecret;

    @Column
    private String scope;

    @Column
    private String authorizedGrantTypes;

    @Column
    private String webServerRedirectUri;

    @Column
    private String authorities;

    @Column
    private Integer accessTokenValidity;

    @Column
    private Integer refreshTokenValidity;

    @Column
    private String additionalInformation;

    @Column
    private String autoApprove;

}
