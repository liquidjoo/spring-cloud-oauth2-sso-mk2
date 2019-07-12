package io.bluemoon.authorizationserver2.domain.oauth;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@ToString(exclude = "oAuthUser")
@Table(name = "oauth_user_role")
public class OAuthUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String role;

    @Column
    private String resourceIds;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private OAuthUser oAuthUser;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
