package io.bluemoon.authorizationserver.service.sso;

import io.bluemoon.authorizationserver.domain.oauth.accesstoken.AccessToken;
import io.bluemoon.authorizationserver.domain.oauth.accesstoken.AccessTokenRepository;
import io.bluemoon.authorizationserver.domain.oauth.client.Client;
import io.bluemoon.authorizationserver.domain.oauth.client.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SsoServiceImpl implements SsoService{
    private AccessTokenRepository accessTokenRepository;
    private ClientRepository clientRepository;

    public SsoServiceImpl(
            AccessTokenRepository accessTokenRepository,
            ClientRepository clientRepository
    ) {
        this.accessTokenRepository = accessTokenRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public AccessToken getAccessToken(String token, String clientId) {
        String tokenId = extractTokenId(token);

        return accessTokenRepository.findByTokenIdAndClientId(tokenId, clientId);
    }

    private String extractTokenId(String value) {
        if (value == null) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not avilable. Fatal (should be in the JDK).");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

    @Override
    @Transactional
    public String logoutAllClient(String clientId, String userName) {
        requestLogoutToAllClients(userName);
        removeAccessToken(userName);

        Optional<Client> client = clientRepository.findById(clientId);
        return client.get().getBaseUri();
    }

    private void requestLogoutToAllClients(String userName) {
        List<AccessToken> tokenList = accessTokenRepository.findByUserName(userName);

        for (AccessToken token : tokenList) {
            requestLogoutToClient(token);
        }

    }

    private void requestLogoutToClient(AccessToken token) {
        Optional<Client> client = clientRepository.findById(token.getClientId());

        String logoutUri = client.get().getLogoutUri();
        String authorizationHeader = null;

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("tokenId", token.getTokenId());
        paramMap.put("userName", token.getUserName());

        // logout 요청
    }

    private int removeAccessToken(String userName) {
        return accessTokenRepository.deleteByUserName(userName);
    }
}
