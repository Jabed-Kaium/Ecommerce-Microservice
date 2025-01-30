package org.example.order.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @Value("${application.config.user-url}")
    private String baseUserUrl;

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    public Optional<UserResponse> getUserById(Long id) {

        String getUserUrl = baseUserUrl + "/" + id;

        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> getUserEntity = new HttpEntity<>(headers);

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                getUserUrl,
                HttpMethod.GET,
                getUserEntity,
                UserResponse.class
        );

        return Optional.ofNullable(response.getBody());
    }

    private String getAccessToken() {
        String tokenUrl = keycloakServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type", "client_credentials");
        tokenRequest.add("client_id", keycloakClientId);
        tokenRequest.add("client_secret", keycloakClientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String> > httpEntity = new HttpEntity<>(tokenRequest, headers);

        var response = restTemplate.postForEntity(tokenUrl, httpEntity, Map.class);
        Map<String, Object> tokenResponse = (Map<String, Object>) response.getBody();

        return (String) tokenResponse.get("access_token");
    }
}
