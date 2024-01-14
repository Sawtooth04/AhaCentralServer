package com.sawtooth.ahacentralserver.services.csrftokensstorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CsrfTokensStorage implements ICsrfTokensStorage {
    @Value("${sys.csrf.token.name}")
    private String tokenCookieName;
    @Value("${sys.csrf.token.header.name}")
    private String tokenCookieHeaderName;
    private static final Map<String, String> tokens;

    static {
        tokens = new HashMap<>();
    }

    public ClientRequest.Builder Consume(ClientRequest previous, ClientRequest.Builder builder) {
        String token = tokens.get(String.join("://", previous.url().getScheme(), previous.url().getAuthority()));

        builder.cookie(tokenCookieName, (token == null) ? "" : token);
        builder.header(tokenCookieHeaderName, (token == null) ? "" : token);
        return builder;
    }

    public void Set(ClientRequest request, MultiValueMap<String, ResponseCookie> cookies) {
        String server = String.join("://", request.url().getScheme(), request.url().getAuthority());
        if (cookies.containsKey(tokenCookieName))
            tokens.put(server, Objects.requireNonNull(cookies.getFirst(tokenCookieName)).getValue());
    }

    @Override
    public String toString() {
        return tokens.toString();
    }
}
