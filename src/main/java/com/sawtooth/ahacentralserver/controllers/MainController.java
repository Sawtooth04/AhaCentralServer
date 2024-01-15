package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.main.MainResponse;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
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
        result.add(linkTo(methodOn(ChunkController.class).Synchronize(null, null)).withRel("chunk-sync"));
        result.add(linkTo(methodOn(FileController.class).Put(null, null)).withRel("file-put"));
        result.add(linkTo(methodOn(FileController.class).Get()).withRel("file-get"));
        result.add(linkTo(methodOn(FileController.class).Patch(null)).withRel("file-patch"));
        result.add(linkTo(methodOn(FileController.class).Delete()).withRel("file-delete"));
        result.add(linkTo(methodOn(RegistrationController.class).Register(null)).withRel("registration"));
        result.add(linkTo(methodOn(LoginController.class).Login(null, null)).withRel("login"));
        result.add(linkTo(methodOn(StorageServerController.class).Put(null)).withRel("storage-server-put"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("storage-servers-conditions"));
        result.add(linkTo(methodOn(CentralServerController.class).General()).withRel("central-server-general"));
        result.add(linkTo(methodOn(CentralServerController.class).AvailableStorageServers()).withRel("central-server-available-servers"));
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}
