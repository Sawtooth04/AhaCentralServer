package com.sawtooth.ahacentralserver.services.uribuilder;

import org.springframework.stereotype.Service;

@Service
public class UriBuilder implements IUriBuilder {
    private String uri;

    @Override
    public IUriBuilder Path(String path) {
        uri = path;
        return this;
    }

    @Override
    public IUriBuilder Param(String name, String value) {
        uri = uri.replace(String.format("{%s}", name), value);
        return this;
    }

    @Override
    public String Uri() {
        return uri;
    }
}
