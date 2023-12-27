package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus.IStorageServerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/storage-server")
public class StorageServerController {
    private final IStorage storage;

    @Autowired
    public StorageServerController(IStorage storage) {
        this.storage = storage;
    }

    @PutMapping("/put")
    @Async
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Put(@RequestBody StorageServer model) {
        RepresentationModel<?> result = new RepresentationModel<>();

        result.add(linkTo(methodOn(StorageServerController.class).Put(null)).withSelfRel());
        try {
            storage.GetRepository(IStorageServerRepository.class).Add(model.WithStorageServerStatusID(
                storage.GetRepository(IStorageServerStatusRepository.class).Get("storage").storageServerStatusID()
            ));
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}
