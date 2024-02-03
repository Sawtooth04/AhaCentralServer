package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.main.MainResponse;
import org.apache.catalina.connector.Response;
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
        result.add(linkTo(methodOn(AuthenticationController.class).Get(null)).withRel("auth-get"));
        result.add(linkTo(methodOn(AuthenticationController.class).Logout(new Response())).withRel("auth-logout"));
        result.add(linkTo(methodOn(LoginController.class).Login(null, null)).withRel("login"));
        result.add(linkTo(methodOn(ChunkController.class).Synchronize(null, null)).withRel("chunk-sync"));
        result.add(linkTo(methodOn(FileController.class).Put(null, null)).withRel("file-put"));
        result.add(linkTo(methodOn(FileController.class).Get()).withRel("file-get"));
        result.add(linkTo(methodOn(FileController.class).Patch()).withRel("file-patch"));
        result.add(linkTo(methodOn(FileController.class).Delete()).withRel("file-delete"));
        result.add(linkTo(methodOn(FileController.class).GetAll()).withRel("file-all-get"));
        result.add(linkTo(methodOn(FileController.class).GetDirectories()).withRel("file-directories-get"));
        result.add(linkTo(methodOn(RegistrationController.class).Register(null)).withRel("registration"));
        result.add(linkTo(methodOn(StorageServerController.class).Get()).withRel("storage-server-get"));
        result.add(linkTo(methodOn(StorageServerController.class).GetBackup()).withRel("storage-server-backup-get"));
        result.add(linkTo(methodOn(StorageServerController.class).Post(null, null)).withRel("storage-server-post"));
        result.add(linkTo(methodOn(StorageServerController.class).GetConditions()).withRel("storage-servers-conditions"));
        result.add(linkTo(methodOn(StorageServerController.class).Delete(null, null)).withRel("storage-server-delete"));
        result.add(linkTo(methodOn(CentralServerController.class).General()).withRel("central-server-general"));
        result.add(linkTo(methodOn(CentralServerController.class).AvailableStorageServers()).withRel("central-server-available-servers"));
        result.add(linkTo(methodOn(GroupController.class).Get(null)).withRel("group-get"));
        result.add(linkTo(methodOn(GroupController.class).GetOwn(null)).withRel("group-own-get"));
        result.add(linkTo(methodOn(GroupController.class).Add(null, null)).withRel("group-post"));
        result.add(linkTo(methodOn(GroupController.class).Patch()).withRel("group-patch"));
        result.add(linkTo(methodOn(GroupController.class).Delete()).withRel("group-delete"));
        result.add(linkTo(methodOn(FileRightController.class).GetAll()).withRel("file-right-all-get"));
        result.add(linkTo(methodOn(GroupFileRightController.class).Post(null, null)).withRel("group-file-right-post"));
        result.add(linkTo(methodOn(GroupFileRightController.class).Get(null, null, null)).withRel("group-file-right-get"));
        result.add(linkTo(methodOn(GroupFileRightController.class).Delete(null, null)).withRel("group-file-right-delete"));
        result.add(linkTo(methodOn(StorageServerStatusController.class).Get()).withRel("storage-server-status-get"));
        result.add(linkTo(methodOn(CustomerRoleController.class).Get(null, null)).withRel("customer-role-get"));
        result.add(linkTo(methodOn(CustomerController.class).GetInfo()).withRel("customer-info-get"));
        result.add(linkTo(methodOn(CustomerController.class).Patch(null, null, null)).withRel("customer-patch"));
        result.add(linkTo(methodOn(CustomerController.class).Delete(null, null)).withRel("customer-delete"));
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}
