package io.github.hkx419.casdoor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "casdoor")
public class CasdoorProperties {
    private String endpoint;
    private String clientId;
    private String clientSecret;
    private String certificate;
    private String organizationName;
    private String applicationName;
    private String redirectUri;
    private String CallbackUrl;
    private String frontendHomeUrl;
    private String internalSecret; // 密钥

}
