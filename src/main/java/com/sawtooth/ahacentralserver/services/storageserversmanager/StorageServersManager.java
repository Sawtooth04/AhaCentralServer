package com.sawtooth.ahacentralserver.services.storageserversmanager;

import com.sawtooth.ahacentralserver.models.storageserver.Space;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServerSpace;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServersSpace;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageServersManager implements IStorageServersManager {
    private final IStorage storage;
    private final WebClient webClient;

    @Autowired
    public StorageServersManager(IStorage storage, WebClient webClient) {
        this.storage = storage;
        this.webClient = webClient;
    }

    private StorageServerSpace GetStorageServerSpace(StorageServer server) {
        try {
            RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
                .retrieve().bodyToMono(RepresentationModel.class).block();

            if (links != null && links.getLink("space").isPresent())
                return webClient.get().uri(String.join("", server.address(), links.getLink("space").orElseThrow().getHref())
                    ).retrieve().bodyToMono(StorageServerSpace.class).block();
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private boolean PingStorageServer(StorageServer server) {
        try {
            webClient.head().uri(String.join("", server.address(), "/api/"))
                .retrieve().bodyToMono(RepresentationModel.class).block();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    @Override
    public StorageServersSpace GetStorageServersSpace() throws InstantiationException {
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
        StorageServerSpace tempStorageServerSpace;
        Space free = new Space(0, 0), total = new Space(0, 0);

        servers.addAll(storage.GetRepository(IStorageServerRepository.class).GetBackup());
        for (StorageServer server : servers) {
            tempStorageServerSpace = GetStorageServerSpace(server);
            if (tempStorageServerSpace != null) {
                free.Add(new Space(tempStorageServerSpace.freeSpace, 0).Normalize());
                total.Add(new Space(tempStorageServerSpace.totalSpace, 0).Normalize());
            }
        }
        if (free.degree == 0 && free.basis == 0)
            return new StorageServersSpace(free, 1);
        return new StorageServersSpace(free, total.Copy().Subtract(free).Divide(total).ToFloat());
    }

    @Override
    public List<StorageServer> GetAvailableStorageServers() throws InstantiationException {
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
        List<StorageServer> result = new ArrayList<>();

        servers.addAll(storage.GetRepository(IStorageServerRepository.class).GetBackup());
        for (StorageServer server : servers)
            if (PingStorageServer(server))
                result.add(server);
        return result;
    }
}
