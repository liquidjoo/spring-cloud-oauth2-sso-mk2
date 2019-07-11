package io.bluemoon.testservice.domain.oauth;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_role", schema = "oauth2")
public class OAuthUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String role;

    @Column
    private String projectId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private OAuthUser oAuthUser;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
