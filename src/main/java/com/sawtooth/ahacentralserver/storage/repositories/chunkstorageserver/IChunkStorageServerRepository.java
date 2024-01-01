package com.sawtooth.ahacentralserver.storage.repositories.chunkstorageserver;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunkstorageserver.ChunkStorageServer;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IChunkStorageServerRepository extends IRepository {
    public void Add(ChunkStorageServer chunkStorageServer);

    public List<ChunkStorageServer> GetByChunk(Chunk chunk);
}
