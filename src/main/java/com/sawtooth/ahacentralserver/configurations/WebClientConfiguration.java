package com.sawtooth.ahacentralserver.configurations;

import com.sawtooth.ahacentralserver.services.csrftokensstorage.ICsrfTokensStorage;
import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {
    public static final int TIMEOUT = 1000;

    @Bean
    public WebClient webClientWithTimeout(ICsrfTokensStorage csrfTokensStorage) {
        HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT);
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(1048576))
            .build();

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(client))
            .filter(CsrfFilter(csrfTokensStorage))
            .exchangeStrategies(strategies)
            .build();
    }

    private ExchangeFilterFunction CsrfFilter(ICsrfTokensStorage csrfTokensStorage) {
        return ((request, next) -> {
            return next.exchange(csrfTokensStorage.Consume(request, ClientRequest.from(request)).build()).flatMap(response -> {
                csrfTokensStorage.Set(request, response.cookies());
                if (response.statusCode().is4xxClientError())
                    return next.exchange(csrfTokensStorage.Consume(request, ClientRequest.from(request)).build()).flatMap(Mono::just);
                return Mono.just(response);
            });
        });
    }
}
