package com.sawtooth.ahacentralserver.services.uribuilder;

public interface IUriBuilder {
    public IUriBuilder Path(String path);

    public IUriBuilder Param(String name, String value);

    public String Uri();
}
