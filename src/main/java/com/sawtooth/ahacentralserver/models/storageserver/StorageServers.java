package com.sawtooth.ahacentralserver.models.storageserver;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

public class StorageServers extends RepresentationModel<StorageServers> {
    public List<StorageServer> servers;

    public StorageServers() {
        servers = new ArrayList<>();
    }
}
