package com.sawtooth.ahacentralserver.services.chunkmissinguploader;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkDownloadModel;
import com.sawtooth.ahacentralserver.models.chunk.ChunkLastModified;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkdataprovider.IChunkDataProvider;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkMissingUploader implements IChunkMissingUploader {
    private final IStorage storage;
    private final IChunkDataProvider chunkDataProvider;

    @Autowired
    public ChunkMissingUploader(IStorage storage, IChunkDataProvider chunkDataProvider) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
    }

    @Override
    public boolean UploadMissing(StorageServer server) throws InstantiationException {
        int chunksPointer = 0;
        List<Chunk> chunks;
        List<StorageServer> chunkStorageServers;
        ChunkDownloadModel downloadModel;

        do {
            chunks = storage.GetRepository(IChunkRepository.class).GetByStorageServer(server, chunksPointer, 10);
            for (Chunk chunk : chunks)
                try {
                    if (!chunkDataProvider.TryGetIsExistsChunk(chunk, server)) {
                        chunkStorageServers = storage.GetRepository(IStorageServerRepository.class).GetByChunk(chunk);
                        chunkStorageServers.removeIf(s -> s.storageServerID() == server.storageServerID());
                        downloadModel = chunkDataProvider.TryDownloadChunkFromServer(chunk, chunkDataProvider
                            .TryGetLastModifiedTimestamp(chunk, chunkStorageServers).server());
                        if (!chunkDataProvider.TryPutChunkToStorageServer(new ChunkUploadModel(chunk.name(), downloadModel.getData()), server))
                            return false;
                    }
                }
                catch (Exception exception) {
                    return false;
                }
            chunksPointer += 10;
        }
        while (!chunks.isEmpty());
        return true;
    }
}
