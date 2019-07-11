package io.bluemoon.testservice.domain.user;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user", schema = "service")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String status;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
