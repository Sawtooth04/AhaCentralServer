package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.centralserver.AvailableStorageServers;
import com.sawtooth.ahacentralserver.models.centralserver.General;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServersSpace;
import com.sawtooth.ahacentralserver.services.storageserversmanager.IStorageServersManager;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/central-server")
public class CentralServerController {
    private final IStorage storage;
    private final IStorageServersManager storageServersManager;

    @Autowired
    public CentralServerController(IStorage storage, IStorageServersManager storageServersManager) {
        this.storage = storage;
        this.storageServersManager = storageServersManager;
    }

    @GetMapping("/general")
    @Async
    public CompletableFuture<ResponseEntity<General>> General() {
        General result = new General();
        StorageServersSpace storageServersSpace;

        try {
            IStorageServerRepository storageServerRepository = storage.GetRepository(IStorageServerRepository.class);
            storageServersSpace = storageServersManager.GetStorageServersSpace();

            result.storageServersCount = storageServerRepository.StorageCount();
            result.backupServersCount = storageServerRepository.BackupCount();
            result.customersCount = storage.GetRepository(ICustomerRepository.class).Count();
            result.chunksCount = storage.GetRepository(IChunkRepository.class).Count();
            result.occupied = storageServersSpace.occupied();
            result.free = storageServersSpace.free();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/storage-servers/available")
    @Async
    public CompletableFuture<ResponseEntity<AvailableStorageServers>> AvailableStorageServers() {
        AvailableStorageServers result = new AvailableStorageServers();

        try {
            result.add(linkTo(methodOn(CentralServerController.class).AvailableStorageServers()).withSelfRel());
            result.servers = storageServersManager.GetAvailableStorageServers();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
