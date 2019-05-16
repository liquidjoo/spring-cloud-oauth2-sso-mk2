package io.bluemoon.authorizationserver.domain.user;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String userName;

    @Column(length = 100, nullable = false)
    private String password;

    //1:수퍼관리자, 2:관리자, 3:사용자
    @Column(length = 1, nullable = false)
    private String userType;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate = new Date();
}
