package com.sawtooth.ahacentralserver.configurations;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableAsync
public class AppConfiguration {
    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;
    private final IStorage storage;

    @Autowired
    public AppConfiguration(IStorage storage) {
        this.storage = storage;
    }

    public String[] GetAllowedOrigins() throws InstantiationException {
        IStorageServerRepository storageServerRepository = storage.GetRepository(IStorageServerRepository.class);
        List<StorageServer> servers = storageServerRepository.Get();
        List<String> result;

        servers.addAll(storageServerRepository.GetBackup());
        result = servers.stream().map(StorageServer::address).collect(Collectors.toList());
        result.addAll(List.of(corsAllowedOrigins));
        return result.toArray(new String[0]);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nonnull CorsRegistry registry) {
                try {
                    registry.addMapping("/**")
                        .allowedOrigins(GetAllowedOrigins())
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
                }
                catch (Exception exception) {
                    registry.addMapping("/**")
                        .allowedOrigins(corsAllowedOrigins)
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
                }
            }
        };
    }
}
