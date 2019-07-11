package io.bluemoon.testservice.domain.oauth;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name = "user", schema = "oauth2")
public class OAuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String ResourceId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oAuthUser", fetch = FetchType.EAGER)
    private Collection<OAuthUserRole> userRole;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}
