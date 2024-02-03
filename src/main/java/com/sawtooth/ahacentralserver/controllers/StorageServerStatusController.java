package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.storageserverstatus.StorageServerStatuses;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus.IStorageServerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/storage-server-status")
public class StorageServerStatusController {
    private final IStorage storage;

    @Autowired
    public StorageServerStatusController(IStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/get")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<StorageServerStatuses>> Get() {
        StorageServerStatuses result = new StorageServerStatuses();

        result.add(linkTo(methodOn(StorageServerStatusController.class).Get()).withSelfRel());
        try {
            result.statuses = storage.GetRepository(IStorageServerStatusRepository.class).Get();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
