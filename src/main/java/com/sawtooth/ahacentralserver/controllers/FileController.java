package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.file.FileUploadModel;
import com.sawtooth.ahacentralserver.services.filedeleter.IFileDeleter;
import com.sawtooth.ahacentralserver.services.fileresourcecomposer.IFileResourceComposer;
import com.sawtooth.ahacentralserver.services.fileupdater.IFileUpdater;
import com.sawtooth.ahacentralserver.services.fileuploader.IFileUploader;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.file.IFileRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.io.FileInputStream;
import java.security.Principal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/file")
public class FileController {
    private final IFileUploader fileUploader;
    private final IFileResourceComposer fileResourceComposer;
    private final IFileUpdater fileUpdater;
    private final IFileDeleter fileDeleter;
    private final IStorage storage;

    @Autowired
    public FileController(IFileUploader fileUploader, IStorage storage, IFileResourceComposer fileResourceComposer,
        IFileUpdater fileUpdater, IFileDeleter fileDeleter) {
        this.fileUploader = fileUploader;
        this.storage = storage;
        this.fileResourceComposer = fileResourceComposer;
        this.fileUpdater = fileUpdater;
        this.fileDeleter = fileDeleter;
    }

    private String GetFilePath(HttpServletRequest request, String mapping) {
        String path = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
            .replace(mapping, "").replaceAll("(/[^/]*)$", "");
        return path.isEmpty() ? "/" : path;
    }

    private String GetFileName(HttpServletRequest request) {
        return ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
            .replaceAll("(.*/)", "");
    }

    @PutMapping("/put")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Put(FileUploadModel model, Principal principal) {
        RepresentationModel<?> result = new RepresentationModel<>();
        int fileID;

        try {
            fileID = storage.GetRepository(IFileRepository.class).Put(new File(-1, storage.GetRepository(ICustomerRepository.class)
                .Get(principal.getName()).customerID(),
                model.file().getOriginalFilename(), model.path(), null, null));
            if (fileUploader.Upload(model, fileID))
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/get")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> Get() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @GetMapping("/get/**")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<Resource>> Get(HttpServletRequest request) {
        java.io.File tempFile;
        String path = GetFilePath(request, "/api/file/get"), name = GetFileName(request);

        try {
            if ((tempFile = fileResourceComposer.Compose(storage.GetRepository(IFileRepository.class).Get(path, name))) != null) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).contentLength(tempFile.length())
                    .header("Content-Disposition", String.format("attachment; filename=\"%s\"", name))
                    .contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(new FileInputStream(tempFile))));
            }
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }

    @PatchMapping("/patch")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Patch(FileUploadModel model) {
        RepresentationModel<?> result = new RepresentationModel<>();
        File file;

        try {
            file = storage.GetRepository(IFileRepository.class).Get(model.path(), model.file().getOriginalFilename());
            if (fileUpdater.Update(model, file))
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (EmptyResultDataAccessException exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
    }

    @GetMapping("/delete")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> Delete() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @DeleteMapping("/delete/**")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Delete(HttpServletRequest request) {
        RepresentationModel<?> result = new RepresentationModel<>();
        String path = GetFilePath(request, "/api/file/delete"), name = GetFileName(request);
        File file;

        try {
            file = storage.GetRepository(IFileRepository.class).Get(path.isEmpty() ? "/" : path, name);
            storage.GetRepository(IFileRepository.class).Delete(file);
            if (fileDeleter.Delete(file))
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (EmptyResultDataAccessException exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
    }
}
