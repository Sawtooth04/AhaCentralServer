package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.file.FilePutModel;
import com.sawtooth.ahacentralserver.services.fileuploader.IFileUploader;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.file.IFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/file")
public class FileController {
    private final IFileUploader fileUploader;
    private final IStorage storage;

    @Autowired
    public FileController(IFileUploader fileUploader, IStorage storage) {
        this.fileUploader = fileUploader;
        this.storage = storage;
    }

    @PutMapping("/put")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Put(FilePutModel model, Principal principal) {
        RepresentationModel<?> result = new RepresentationModel<>();
        int fileID;

        try {
            fileID = storage.GetRepository(IFileRepository.class).Put(new File(-1, storage.GetRepository(ICustomerRepository.class)
                .Get(principal.getName()).customerID(),
                model.file().getOriginalFilename(), model.path(), null, null));
            fileUploader.Upload(model, fileID);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
