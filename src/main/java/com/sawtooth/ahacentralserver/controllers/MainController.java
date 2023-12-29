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
        result.add(linkTo(methodOn(StorageServerController.class).Put(null)).withRel("storage-server-put"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("storage-servers-conditions"));
        result.add(linkTo(methodOn(RegistrationController.class).Register(null)).withRel("registration"));
        result.add(linkTo(methodOn(LoginController.class).Login(null, null)).withRel("login"));
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}
