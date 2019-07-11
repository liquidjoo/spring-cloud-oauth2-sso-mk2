package io.bluemoon.authorizationserver2.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
}
