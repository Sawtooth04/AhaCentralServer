package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.chunk.ChunkSynchronizationModel;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadMissingModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkmissinguploader.IChunkMissingUploader;
import com.sawtooth.ahacentralserver.services.chunksynchronizer.IChunkSynchronizer;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/chunk")
public class ChunkController {
    private final IStorage storage;
    private final IChunkSynchronizer chunkSynchronizer;
    private final IChunkMissingUploader chunkMissingUploader;

    @Autowired
    public ChunkController(IStorage storage, IChunkSynchronizer chunkSynchronizer, IChunkMissingUploader chunkMissingUploader) {
        this.storage = storage;
        this.chunkSynchronizer = chunkSynchronizer;
        this.chunkMissingUploader = chunkMissingUploader;
    }

    @PostMapping("/synchronize")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Synchronize(@RequestBody ChunkSynchronizationModel model,
        HttpServletRequest request) {
        StorageServer server;
        RepresentationModel<?> result = new RepresentationModel<>();

        try {
            server = storage.GetRepository(IStorageServerRepository.class).Get(String.join(":",
                request.getRemoteAddr(), Integer.toString(model.port())));
            if (chunkSynchronizer.Synchronize(model, server))
                return CompletableFuture.completedFuture(ResponseEntity.ok(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
    }

    @PostMapping("/upload-missing")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> UploadMissing(@RequestBody ChunkUploadMissingModel model) {
        StorageServer server;
        RepresentationModel<?> result = new RepresentationModel<>();

        try {
            server = storage.GetRepository(IStorageServerRepository.class).Get(model.serverID());
            if (chunkMissingUploader.UploadMissing(server))
                return CompletableFuture.completedFuture(ResponseEntity.ok(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
    }
}
