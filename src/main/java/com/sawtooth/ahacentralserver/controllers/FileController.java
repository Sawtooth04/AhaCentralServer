package com.sawtooth.ahacentralserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.sawtooth.ahacentralserver.models.file.DirectoryItems;
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
            .replace(mapping, "").replaceAll("(/[^/]*)$", "").replace("/root", "");
        return path.isEmpty() ? "/" : path.replaceAll("%20", " ");
    }

    private String GetFileName(HttpServletRequest request) {
        return ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
            .replaceAll("(.*/)", "");
    }

    @PutMapping("/file/put")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Put(FileUploadModel model, Principal principal) {
        RepresentationModel<?> result = new RepresentationModel<>();
        File file;

        try {
            if (storage.GetRepository(IFileRepository.class).IsFileExists(model.file().getOriginalFilename(), model.path())) {
                file = storage.GetRepository(IFileRepository.class).Get(model.path(), model.file().getOriginalFilename());
                if (fileUpdater.Update(model, file))
                    return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
            }
            else if (fileUploader.Upload(model, storage.GetRepository(ICustomerRepository.class).Get(principal.getName()).customerID()))
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CREATED).body(result));
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/file/get")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> Get() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @GetMapping("/file/get/**")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<Resource>> Get(HttpServletRequest request) {
        java.io.File tempFile;
        String path = GetFilePath(request, "/api/file/file/get"), name = GetFileName(request);

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

    @GetMapping("/file/patch")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> Patch() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @PatchMapping("/file/patch/**")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Patch(@RequestBody JsonPatch patch, HttpServletRequest request) {
        RepresentationModel<?> result = new RepresentationModel<>();
        String path = GetFilePath(request, "/api/file/file/patch"), name = GetFileName(request);
        ObjectMapper objectMapper = new ObjectMapper();
        File file;

        try {
            file = storage.GetRepository(IFileRepository.class).Get(path, name);
            storage.GetRepository(IFileRepository.class).Update(objectMapper.treeToValue(patch.apply(
                objectMapper.convertValue(file, JsonNode.class)), File.class));
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NO_CONTENT).body(result));
        }
        catch (EmptyResultDataAccessException exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/file/delete")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> Delete() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @DeleteMapping("/file/delete/**")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Delete(HttpServletRequest request) {
        RepresentationModel<?> result = new RepresentationModel<>();
        String path = GetFilePath(request, "/api/file/file/delete"), name = GetFileName(request);
        File file;

        try {
            file = storage.GetRepository(IFileRepository.class).Get(path, name);
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

    @GetMapping("/all/get")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> GetAll() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @GetMapping("/all/get/**")
    @ResponseBody
    public CompletableFuture<ResponseEntity<DirectoryItems>> GetAll(HttpServletRequest request) {
        String path = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
            .replace("/api/file/all/get/", "").replace("root", "/")
            .replace("//", "/").replace("%20", " ");
        DirectoryItems result = new DirectoryItems();
        IFileRepository fileRepository;

        try {
            fileRepository = storage.GetRepository(IFileRepository.class);
            result.items = fileRepository.GetDirectories(path);
            result.items.addAll(fileRepository.GetFiles(path));
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(result));
        }
    }

    @GetMapping("/directories/get")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> GetDirectories() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @GetMapping("/directories/get/**")
    @ResponseBody
    public CompletableFuture<ResponseEntity<DirectoryItems>> GetDirectories(HttpServletRequest request) {
        String path = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
            .replace("/api/file/directories/get/", "").replace("root", "/")
            .replace("//", "/").replace("%20", " ");
        DirectoryItems result = new DirectoryItems();
        IFileRepository fileRepository;

        try {
            fileRepository = storage.GetRepository(IFileRepository.class);
            result.items = fileRepository.GetDirectories(path);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(result));
        }
    }
}
