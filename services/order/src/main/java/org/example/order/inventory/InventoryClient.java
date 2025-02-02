package org.example.order.inventory;

import lombok.RequiredArgsConstructor;
import org.example.commondto.order.OrderItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryClient {

    private final RestTemplate restTemplate;

    @Value("${application.config.product-url}")
    private String productBaseUrl;

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    public void purchaseProducts(List<OrderItem> orderItems) {

        String getProductUrl = productBaseUrl + "/purchase";

        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Content-Type", "application/json");

        HttpEntity<List<OrderItem>> getProductEntity = new HttpEntity<>(orderItems, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                getProductUrl,
                HttpMethod.POST,
                getProductEntity,
                Void.class
        );
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
