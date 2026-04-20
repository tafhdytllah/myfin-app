package com.tafh.myfin_app.seed;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.seed")
public class DevSeedProperties {

    private boolean enabled;

    private String demoUsername;

    private String demoEmail;

    private String demoPassword;
}
