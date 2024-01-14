package com.sawtooth.ahacentralserver.services.csrftokensstorage;

import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;

public interface ICsrfTokensStorage {
    public ClientRequest.Builder Consume(ClientRequest previous, ClientRequest.Builder builder);

    public void Set(ClientRequest request, MultiValueMap<String, ResponseCookie> cookies);
}
