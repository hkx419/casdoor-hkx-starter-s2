package io.github.hkx419.casdoor;

import org.casbin.casdoor.config.Config;
import org.casbin.casdoor.service.AuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CasdoorProperties.class)
@ComponentScan("io.github.hkx419.casdoor") // 扫描 Controller
public class CasdoorAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "casdoor", name = "endpoint")
    public AuthService authService(CasdoorProperties properties) {
        Config config = new Config(
                properties.getEndpoint(),
                properties.getClientId(),
                properties.getClientSecret(),
                properties.getCertificate(),
                properties.getOrganizationName(),
                properties.getApplicationName()
        );
        return new AuthService(config);
    }
}