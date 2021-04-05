package com.example.project.ecomm.ECommerce.JWT;

import com.google.common.net.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration

@ConfigurationProperties(prefix = "application.jwt")

public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private int tokenExpirationDays;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getTokenPrefix() {
        return this.tokenPrefix;
    }

    public int getTokenExpirationDays() {
        return this.tokenExpirationDays;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public void setTokenExpirationDays(int tokenExpirationDays) {
        this.tokenExpirationDays = tokenExpirationDays;
    }
}
