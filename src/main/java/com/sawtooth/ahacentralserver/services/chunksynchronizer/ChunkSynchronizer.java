package com.sawtooth.ahacentralserver.services.chunksynchronizer;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkLastModified;
import com.sawtooth.ahacentralserver.models.chunk.ChunkSynchronizationModel;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkdataprovider.IChunkDataProvider;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkSynchronizer implements IChunkSynchronizer {
    private final IStorage storage;
    private final IChunkDataProvider chunkDataProvider;

    @Autowired
    public ChunkSynchronizer(IStorage storage, IChunkDataProvider chunkDataProvider) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
    }

    private void BroadcastChunk(ChunkSynchronizationModel model, StorageServer from, List<StorageServer> servers) {
        servers.removeIf(s -> s.storageServerID() == from.storageServerID());
        chunkDataProvider.TryPutChunk(new ChunkUploadModel(model.name(), model.data()), servers);
    }

    private void SaveChunk(Chunk chunk, ChunkLastModified lastModified, StorageServer server) {
        chunkDataProvider.TryPutChunkToStorageServer(
            new ChunkUploadModel(chunk.name(), chunkDataProvider.TryDownloadChunkFromServer(chunk, lastModified.server()).getData()),
            server
        );
    }

    private boolean DeleteChunk(ChunkSynchronizationModel model, StorageServer server) {
        return chunkDataProvider.TryDeleteChunkFromStorageServer(
            new Chunk(-1, -1, model.name(), -1, -1),
            server
        );
    }

    @Override
    public boolean Synchronize(ChunkSynchronizationModel model, StorageServer server) throws InstantiationException {
        Chunk chunk;
        ChunkLastModified lastModified;
        List<StorageServer> chunkStorageServers;
        IStorageServerRepository storageServerRepository = storage.GetRepository(IStorageServerRepository.class);

        try {
            chunk = storage.GetRepository(IChunkRepository.class).GetByName(model.name());
            chunkStorageServers = storageServerRepository.GetByChunk(chunk);
            chunkStorageServers.removeIf(s -> s.storageServerID() == server.storageServerID());
            lastModified = chunkDataProvider.TryGetLastModifiedTimestamp(chunk, chunkStorageServers);
            if (lastModified.lastModified() != 0) {
                if (model.lastModified() > lastModified.lastModified())
                    BroadcastChunk(model, server, chunkStorageServers);
                else if (model.lastModified() < lastModified.lastModified())
                    SaveChunk(chunk, lastModified, server);
            }
            return true;
        }
        catch (EmptyResultDataAccessException exception) {
            return DeleteChunk(model, server);
        }
        catch (Exception exception) {
            return false;
        }
    }
}
