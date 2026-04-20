package com.tafh.myfin_app.seed;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@EnableConfigurationProperties(DevSeedProperties.class)
public class DevSeedConfig {
}
