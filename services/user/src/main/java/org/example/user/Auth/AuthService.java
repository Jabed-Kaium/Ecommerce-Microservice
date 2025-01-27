package org.example.user.Auth;

import lombok.RequiredArgsConstructor;
import org.example.user.exception.UserRegistrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
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

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${keycloak.user-role-id}")
    private String userRoleId;

    @Value("${keycloak.user-role-name}")
    private String userRoleName;

    public String register(RegisterRequest request) {
        try {
            System.out.println("Hit Register Service");
            String adminAccessToken = getAdminAccessToken();

            String createUserUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users";
            Map<String, Object> userPayload = new HashMap<>();
            userPayload.put("username", request.username());
            userPayload.put("email", request.email());
            userPayload.put("firstName", request.firstname());
            userPayload.put("lastName", request.lastname());
            userPayload.put("enabled", true);

            Map<String, Object> credentials = new HashMap<>();
            credentials.put("type", "password");
            credentials.put("value", request.password());
            credentials.put("temporary", false);

            userPayload.put("credentials", new Map[]{credentials});

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminAccessToken);
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(userPayload, headers);

            ResponseEntity<String> createUserResponse = restTemplate.postForEntity(createUserUrl, httpEntity, String.class);

            if(createUserResponse.getStatusCode() != HttpStatus.CREATED) {
                throw new UserRegistrationException("Failed to create user");
            }

            System.out.println("User created");

//            String getUsersUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users?username=" + request.username();
//            HttpEntity<Void> getUserEntity = new HttpEntity<>(headers);
//
//            ResponseEntity<Object[]> getUserResponse = restTemplate.exchange(
//                    getUsersUrl,
//                    HttpMethod.GET,
//                    getUserEntity,
//                    Object[].class
//            );
//
//            if(getUserResponse.getBody() == null || getUserResponse.getBody().length == 0) {
//                throw new UserRegistrationException("User not found after creation");
//            }
//            Map<String, Object> userResponse = (Map<String, Object>) getUserResponse.getBody()[0];
//            String userId = (String) userResponse.get("id");
//
//            String assignRoleUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId + "/role-mappings/realm";
//
//            Map<String, Object> rolePayload = new HashMap<>();
//            rolePayload.put("id", userRoleId);
//            rolePayload.put("name", userRoleName);
//
//            HttpEntity<Map<String, Object>[]> assignRoleEntity = new HttpEntity<>(new Map[]{rolePayload}, headers);
//
//            ResponseEntity<Void> assignRoleResponse = restTemplate.postForEntity(assignRoleUrl, assignRoleEntity, Void.class);
//
//            if(assignRoleResponse.getStatusCode() == HttpStatus.NO_CONTENT) {
//                throw new UserRegistrationException("Failed to assign role to user");
//            }

            return "User registered successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new UserRegistrationException("Failed to register user");
        }
    }

    private String getAdminAccessToken() {
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
