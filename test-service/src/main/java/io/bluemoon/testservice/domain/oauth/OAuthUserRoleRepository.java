package io.bluemoon.testservice.domain.oauth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthUserRoleRepository extends JpaRepository<OAuthUserRole, Integer> {
}
