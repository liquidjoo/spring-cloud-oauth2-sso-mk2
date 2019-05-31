package io.bluemoon.authorizationserver.domain.user;

import io.bluemoon.authorizationserver.domain.social.SocialType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String principal;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<UserRole> userRole;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;



//    //1:수퍼관리자, 2:관리자, 3:사용자
//    @Column
//    private String userType;

//    @Column
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date regDate = new Date();

    @Builder
    public User(String username, String name, String password, String email, String principal, LocalDateTime createdAt, LocalDateTime updatedAt, SocialType socialType) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.principal = principal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.socialType = socialType;
    }
}
