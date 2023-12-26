package com.sawtooth.ahacentralserver.configurations;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
public class AppConfiguration {
    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nonnull CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins(corsAllowedOrigins)
                    .allowCredentials(true)
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
