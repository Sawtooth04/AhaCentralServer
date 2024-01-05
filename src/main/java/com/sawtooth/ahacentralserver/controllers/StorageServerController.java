package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.models.storageservercondition.StorageServerCondition;
import com.sawtooth.ahacentralserver.models.storageservercondition.StorageServersConditions;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus.IStorageServerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/storage-server")
public class StorageServerController {
    private final IStorage storage;
    private final WebClient webClient;

    @Autowired
    public StorageServerController(IStorage storage, WebClient webClient) {
        this.storage = storage;
        this.webClient = webClient;
    }

    private StorageServerCondition GetStorageServerCondition(StorageServer server) {
        RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
            .retrieve().bodyToMono(RepresentationModel.class).block();

        if (links != null && links.getLink("system-info").isPresent())
            return Objects.requireNonNull(webClient.get().uri(String.join("", server.address(),
                links.getLink("system-info").get().getHref())).retrieve().bodyToMono(StorageServerCondition.class)
                .block()).WithStorageServer(server);
        return null;
    }

    @PutMapping("/put")
    @Async
    @ResponseBody
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

    @GetMapping("/condition/get")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<RepresentationModel<StorageServersConditions>>> GetConditions() {
        StorageServersConditions result = new StorageServersConditions();

        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withSelfRel());
        result.add(linkTo(methodOn(StorageServerController.class).Put(null)).withRel("put"));
        try {
            List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
            for (StorageServer server : servers)
                result.conditions.add(GetStorageServerCondition(server));
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}
