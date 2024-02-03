package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServers;
import com.sawtooth.ahacentralserver.models.storageservercondition.StorageServerCondition;
import com.sawtooth.ahacentralserver.models.storageservercondition.StorageServersConditions;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;
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

    @GetMapping("/storage/get")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<StorageServers>> Get() {
        StorageServers result = new StorageServers();

        result.add(linkTo(methodOn(StorageServerController.class).Get()).withSelfRel());
        result.add(linkTo(methodOn(StorageServerController.class).GetBackup()).withRel("get-backup"));
        result.add(linkTo(methodOn(StorageServerController.class).Post(null, null)).withRel("post"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("get-conditions"));
        result.add(linkTo(methodOn(StorageServerController.class).Delete(null, null)).withRel("delete"));
        try {
            result.servers = storage.GetRepository(IStorageServerRepository.class).Get();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/backup/get")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<StorageServers>> GetBackup() {
        StorageServers result = new StorageServers();

        result.add(linkTo(methodOn(StorageServerController.class).Get()).withRel("get"));
        result.add(linkTo(methodOn(StorageServerController.class).GetBackup()).withSelfRel());
        result.add(linkTo(methodOn(StorageServerController.class).Post(null, null)).withRel("post"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("get-conditions"));
        result.add(linkTo(methodOn(StorageServerController.class).Delete(null, null)).withRel("delete"));
        try {
            result.servers = storage.GetRepository(IStorageServerRepository.class).GetBackup();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @PostMapping("/post")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Post(Principal principal, @RequestBody StorageServer model) {
        RepresentationModel<?> result = new RepresentationModel<>();
        ICustomerRepository customerRepository;

        result.add(linkTo(methodOn(StorageServerController.class).Get()).withRel("get"));
        result.add(linkTo(methodOn(StorageServerController.class).GetBackup()).withRel("get-backup"));
        result.add(linkTo(methodOn(StorageServerController.class).Post(null, null)).withSelfRel());
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("get-conditions"));
        result.add(linkTo(methodOn(StorageServerController.class).Delete(null, null)).withRel("delete"));
        try {
            customerRepository = storage.GetRepository(ICustomerRepository.class);
            if (customerRepository.IsCustomerHaveRole(customerRepository.Get(principal.getName()), "admin")) {
                storage.GetRepository(IStorageServerRepository.class).Add(model);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
            }
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/condition/get")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<RepresentationModel<StorageServersConditions>>> GetConditions() {
        StorageServersConditions result = new StorageServersConditions();

        result.add(linkTo(methodOn(StorageServerController.class).Get()).withRel("get"));
        result.add(linkTo(methodOn(StorageServerController.class).GetBackup()).withRel("get-backup"));
        result.add(linkTo(methodOn(StorageServerController.class).Post(null, null)).withRel("post"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withSelfRel());
        result.add(linkTo(methodOn(StorageServerController.class).Delete(null, null)).withRel("delete"));
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

    @DeleteMapping("/delete/{storageServerID}")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Delete(Principal principal, @PathVariable Integer storageServerID) {
        RepresentationModel<?> result = new RepresentationModel<>();
        ICustomerRepository customerRepository;

        result.add(linkTo(methodOn(StorageServerController.class).Get()).withRel("get"));
        result.add(linkTo(methodOn(StorageServerController.class).GetBackup()).withRel("get-backup"));
        result.add(linkTo(methodOn(StorageServerController.class).Post(null, null)).withRel("post"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("get-conditions"));
        result.add(linkTo(methodOn(StorageServerController.class).Delete(null, null)).withSelfRel());
        try {
            customerRepository = storage.GetRepository(ICustomerRepository.class);
            if (customerRepository.IsCustomerHaveRole(customerRepository.Get(principal.getName()), "admin")) {
                storage.GetRepository(IStorageServerRepository.class).Delete(storageServerID);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
            }
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
