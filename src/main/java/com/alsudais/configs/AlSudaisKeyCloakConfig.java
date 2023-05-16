package com.alsudais.configs;

import com.alsudais.commons.AlSudaisCommonConstants;
import lombok.Data;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Data
@Configuration
public class AlSudaisKeyCloakConfig {

    @Value("${keycloak.auth-server-url:http://15.185.80.24:8080/auth}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm:ALSUDAIS}")
    private String keycloakRealm;

    @Value("${keycloak.resource:ALSUDAIS_CLIENT}")
    private String keycloakResource;

    @Value("${keycloak.credentials.secret:jRZQ7d19smU9hz4HsGkQNaRiWcDcdhKa}")
    private String keycloakCredentialsSecret;

    @Value("${keycloak.connection.pool.size:10}")
    private Integer keycloakConnectionPoolSize;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder().realm(this.keycloakRealm)
                .serverUrl(this.keycloakAuthServerUrl)
                .clientId(this.keycloakResource)
                .clientSecret(this.keycloakCredentialsSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(this.keycloakConnectionPoolSize).build())
                .build();
    }

    @Bean
    public AuthzClient authzClient() {
        return AuthzClient.create(new org.keycloak.authorization.client.Configuration(this.keycloakAuthServerUrl,
                this.keycloakRealm,
                this.keycloakResource,
                Map.of("secret", this.keycloakCredentialsSecret, "grant_type", AlSudaisCommonConstants.INSTANCE.PASSWORD_STRING),
                null));
    }
}