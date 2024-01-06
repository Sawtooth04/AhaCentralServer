package com.sawtooth.ahacentralserver.services.chunkdataprovider;

import com.sawtooth.ahacentralserver.models.chunk.*;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.uribuilder.IUriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ChunkDataProvider implements IChunkDataProvider {
    private final WebClient webClient;
    private final IUriBuilder uriBuilder;
    @Value("${sys.pool.max-threads-count}")
    private int poolThreadsCount;

    @Autowired
    public ChunkDataProvider(WebClient webClient, IUriBuilder uriBuilder) {
        this.webClient = webClient;
        this.uriBuilder = uriBuilder;
    }

    @Override
    public ChunkDownloadModel TryDownloadChunkFromServer(Chunk chunk, StorageServer server) {
        RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
            .retrieve().bodyToMono(RepresentationModel.class).block();

        if (links != null && links.getLink("chunk-get").isPresent())
            try {
                return webClient.get().uri(uriBuilder
                    .Path(String.join("", server.address(), links.getLink("chunk-get").orElseThrow().getHref()))
                    .Param("name", chunk.name())
                    .Uri()).retrieve().bodyToMono(ChunkDownloadModel.class).block();
            }
            catch (Exception exception) {
                return null;
            }
        return null;
    }

    @Override
    public ChunkDownloadModel TryDownloadChunk(Chunk chunk, List<StorageServer> servers) {
        ChunkDownloadModel chunkDownloadModel;

        for (StorageServer server : servers)
            if ((chunkDownloadModel = TryDownloadChunkFromServer(chunk, server)) != null)
                return chunkDownloadModel;
        return null;
    }

    @Override
    public boolean TryPutChunkToStorageServer(ChunkUploadModel chunkUploadModel, StorageServer server) {
        try {
            RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
                .retrieve().bodyToMono(RepresentationModel.class).block();

            if (links != null && links.getLink("chunk-put").isPresent()) {
                webClient.put().uri(String.join("", server.address(), links.getLink("chunk-put").get()
                    .getHref())).bodyValue(chunkUploadModel).retrieve().bodyToMono(RepresentationModel.class).block();
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }

    @Override
    public ChunkTryPutResponse TryPutChunk(ChunkUploadModel chunkUploadModel, List<StorageServer> servers, int preferredServerPointer) {
        int attempts = 0, i = preferredServerPointer;

        while (!TryPutChunkToStorageServer(chunkUploadModel, servers.get(i)) && attempts < servers.size()) {
            attempts++;
            i = (i >= servers.size() - 1) ? 0 : i + 1;
        }
        return new ChunkTryPutResponse(attempts < servers.size(), servers.get(i));
    }

    @Override
    public boolean TryPutChunk(ChunkUploadModel chunkUploadModel, List<StorageServer> servers) {
        ExecutorService threadPool = Executors.newFixedThreadPool(poolThreadsCount);
        ChunkPutLock lock = new ChunkPutLock();

        for (StorageServer server : servers)
            threadPool.submit(() -> {
                boolean isPut = TryPutChunkToStorageServer(chunkUploadModel, server);
                synchronized (lock) {
                    lock.result |= isPut;
                }
            });
        threadPool.shutdown();
        return lock.result;
    }

    @Override
    public boolean TryDeleteChunkFromStorageServer(Chunk chunk, StorageServer server) {
        try {
            RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
                .retrieve().bodyToMono(RepresentationModel.class).block();

            if (links != null && links.getLink("chunk-delete").isPresent()) {
                webClient.delete().uri(uriBuilder
                    .Path(String.join("", server.address(), links.getLink("chunk-delete").orElseThrow().getHref()))
                    .Param("name", chunk.name())
                    .Uri()
                ).retrieve().bodyToMono(RepresentationModel.class).block();
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }

    @Override
    public boolean TryDeleteChunk(Chunk chunk, List<StorageServer> servers) {
        boolean result = false;

        for (StorageServer server : servers)
            result |= TryDeleteChunkFromStorageServer(chunk, server);
        return result;
    }

    @Override
    public ChunkLastModified TryGetLastModifiedTimestampFromStorageServer(Chunk chunk, StorageServer server) {
        try {
            RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
                .retrieve().bodyToMono(RepresentationModel.class).block();

            if (links != null && links.getLink("chunk-modified-get").isPresent()) {
                return new ChunkLastModified(Objects.requireNonNull(webClient.get().uri(uriBuilder
                    .Path(String.join("", server.address(), links.getLink("chunk-modified-get").orElseThrow().getHref()))
                    .Param("name", chunk.name())
                    .Uri()
                ).retrieve().bodyToMono(ChunkLastModifiedResponse.class).block()).getLastModified(), server);
            }
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    @Override
    public ChunkLastModified TryGetLastModifiedTimestamp(Chunk chunk, List<StorageServer> servers) {
        ChunkLastModified lastModified = new ChunkLastModified(0L, null), response;

        for (StorageServer server : servers) {
            response = TryGetLastModifiedTimestampFromStorageServer(chunk, server);
            lastModified = (response != null && response.lastModified() > lastModified.lastModified()) ? response : lastModified;
        }
        return lastModified;
    }
}
