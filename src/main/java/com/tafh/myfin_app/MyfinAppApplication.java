package com.tafh.myfin_app;

import com.tafh.myfin_app.config.properties.JwtProperties;
import com.tafh.myfin_app.config.properties.CookieProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, CookieProperties.class})
public class MyfinAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyfinAppApplication.class, args);
    }

}
