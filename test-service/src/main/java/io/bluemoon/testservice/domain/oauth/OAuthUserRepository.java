package io.bluemoon.testservice.domain.oauth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, Integer> {

    Optional<OAuthUser> findByUsername(String username);

}
