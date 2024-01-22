package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.main.MainResponse;
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
@RequestMapping("/api")
public class MainController {
    @GetMapping("/")
    @Async
    public CompletableFuture<ResponseEntity<MainResponse>> Main() {
        MainResponse result = new MainResponse();

        result.add(linkTo(methodOn(MainController.class).Main()).withSelfRel());
        result.add(linkTo(methodOn(LoginController.class).Login(null, null)).withRel("login"));
        result.add(linkTo(methodOn(ChunkController.class).Synchronize(null, null)).withRel("chunk-sync"));
        result.add(linkTo(methodOn(FileController.class).Put(null, null)).withRel("file-put"));
        result.add(linkTo(methodOn(FileController.class).Get()).withRel("file-get"));
        result.add(linkTo(methodOn(FileController.class).Patch()).withRel("file-patch"));
        result.add(linkTo(methodOn(FileController.class).Delete()).withRel("file-delete"));
        result.add(linkTo(methodOn(FileController.class).GetAll()).withRel("file-all-get"));
        result.add(linkTo(methodOn(FileController.class).GetDirectories()).withRel("file-directories-get"));
        result.add(linkTo(methodOn(RegistrationController.class).Register(null)).withRel("registration"));
        result.add(linkTo(methodOn(StorageServerController.class).Put(null)).withRel("storage-server-put"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("storage-servers-conditions"));
        result.add(linkTo(methodOn(CentralServerController.class).General()).withRel("central-server-general"));
        result.add(linkTo(methodOn(CentralServerController.class).AvailableStorageServers()).withRel("central-server-available-servers"));
        result.add(linkTo(methodOn(GroupController.class).Get(null)).withRel("group-get"));
        result.add(linkTo(methodOn(GroupController.class).GetOwn(null)).withRel("group-own-get"));
        result.add(linkTo(methodOn(GroupController.class).Add(null, null)).withRel("group-post"));
        result.add(linkTo(methodOn(GroupController.class).Patch()).withRel("group-patch"));
        result.add(linkTo(methodOn(GroupController.class).Delete()).withRel("group-delete"));
        result.add(linkTo(methodOn(FileRightController.class).GetAll()).withRel("file-right-all-get"));
        result.add(linkTo(methodOn(GroupFileRightController.class).Put(null)).withRel("group-file-right-map-put"));
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}
