package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.fileright.FileRights;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.fileright.IFileRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/file-right")
public class FileRightController {
    private final IStorage storage;

    @Autowired
    public FileRightController(IStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/get")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<FileRights>> GetAll() {
        FileRights result = new FileRights();

        try {
            result.add(linkTo(methodOn(FileRightController.class).GetAll()).withSelfRel());
            result.fileRights = storage.GetRepository(IFileRightRepository.class).GetAll();
            return CompletableFuture.completedFuture(ResponseEntity.ok(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().body(result));
        }
    }
}
