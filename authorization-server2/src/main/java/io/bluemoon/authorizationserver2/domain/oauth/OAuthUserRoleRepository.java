package io.bluemoon.authorizationserver2.domain.oauth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OAuthUserRoleRepository extends JpaRepository<OAuthUserRole, Integer> {
    List<OAuthUserRole> findByOAuthUser(OAuthUser user);
}
