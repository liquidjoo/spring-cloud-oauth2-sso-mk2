package io.bluemoon.authorizationserver.domain.user;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @Builder
    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }
}
