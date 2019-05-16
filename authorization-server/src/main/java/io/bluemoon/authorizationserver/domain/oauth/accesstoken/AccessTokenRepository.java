package io.bluemoon.authorizationserver.domain.oauth.accesstoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

    AccessToken findByTokenIdAndClientId(String tokenId, String clientId);

    int deleteByUserName(String userName);

    List<AccessToken> findByUserName(String username);
}
