package io.bluemoon.authorizationserver.domain.user;

import io.bluemoon.authorizationserver.domain.social.SocialType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String principal;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

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
    public User(String username, String password, String email, String principal,
                SocialType socialType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
