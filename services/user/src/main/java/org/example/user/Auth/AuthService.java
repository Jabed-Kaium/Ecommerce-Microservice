package org.example.user.Auth;

import lombok.RequiredArgsConstructor;
import org.example.user.exception.UserRegistrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${kecloak.client-secret}")
    private String keycloakClientSecret;

    public String register(RegisterRequest request) {
        try {
            String adminAccessToken = getAdminAccessToken();

            String createUserUrl = keycloakServerUrl + "/auth/admin/realms/" + keycloakRealm + "/users";
            Map<String, Object> userPayload = new HashMap<>();
            userPayload.put("username", request.username());
            userPayload.put("email", request.email());
            userPayload.put("firstName", request.firstname());
            userPayload.put("lastName", request.lastname());
            userPayload.put("enabled", true);
            userPayload.put("credentials", new HashMap<>() {{
                put("type", "password");
                put("value", request.password());
                put("temporary", false);
            }});

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminAccessToken);
            headers.set("Content-Type", "application/json");

            HttpEntity httpEntity = new HttpEntity<>(userPayload, headers);

            restTemplate.postForEntity(createUserUrl, httpEntity, String.class);

            return "User registered successfully";
        } catch (Exception e) {
            throw new UserRegistrationException("Failed to register user");
        }
    }

    private String getAdminAccessToken() {
        String tokenUrl = keycloakServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("grant_type", "client_credentials");
        tokenRequest.put("client_id", keycloakClientId);
        tokenRequest.put("client_secret", keycloakClientSecret);

        HttpEntity httpEntity = new HttpEntity<>(tokenRequest);

        var response = restTemplate.postForEntity(tokenUrl, httpEntity, Map.class);
        Map<String, Object> tokenResponse = (Map<String, Object>) response.getBody();

        return (String) tokenResponse.get("access_token");
    }
}
