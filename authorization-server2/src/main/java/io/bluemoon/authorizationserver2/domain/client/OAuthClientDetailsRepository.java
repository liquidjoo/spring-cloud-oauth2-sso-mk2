package io.bluemoon.authorizationserver2.domain.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthClientDetailsRepository extends JpaRepository<OAuthClientDetails, String> {

    Optional<OAuthClientDetails> findByClientId(String clientId);
}
