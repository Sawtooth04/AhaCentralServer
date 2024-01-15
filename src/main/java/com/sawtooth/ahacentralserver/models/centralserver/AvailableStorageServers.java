package com.sawtooth.ahacentralserver.models.centralserver;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class AvailableStorageServers extends RepresentationModel<AvailableStorageServers> {
    public List<StorageServer> servers;
}
