package com.sawtooth.ahacentralserver.configurations;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {
    public static final int TIMEOUT = 1000;

    @Bean
    public WebClient webClientWithTimeout() {
        HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT);

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(client))
            .build();
    }
}
