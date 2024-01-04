package com.sawtooth.ahacentralserver.services.chunksynchronizer;

import com.sawtooth.ahacentralserver.models.chunk.ChunkSynchronizationModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

public interface IChunkSynchronizer {
    public boolean Synchronize(ChunkSynchronizationModel model, StorageServer server) throws InstantiationException;
}
