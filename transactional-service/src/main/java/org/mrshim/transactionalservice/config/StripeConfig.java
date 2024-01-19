package org.mrshim.transactionalservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "stripe")
public class StripeConfig {
    private String secretKey;
    private String publicKey;
}
