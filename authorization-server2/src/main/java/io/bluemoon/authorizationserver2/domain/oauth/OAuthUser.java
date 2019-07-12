package io.bluemoon.authorizationserver2.domain.oauth;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name = "oauth_user")
public class OAuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String status;

    @Column
    private String reSellerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oAuthUser", fetch = FetchType.EAGER)
    private Collection<OAuthUserRole> userRole;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}
