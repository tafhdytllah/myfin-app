package com.tafh.myfin_app.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Myfin Tracker API Documentation")
                        .version("1.0")
                        .description("This is the API documentation for Myfin Tracker API")
                        .contact(new Contact()
                                .name("Taufik Hidayatullah")
                                .email("taufikhh.08@gmail.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Myfin Tracker API Documentation")
                        .url("https://github.com/tafhdytllah/myfin-app")
                );
    }
}
