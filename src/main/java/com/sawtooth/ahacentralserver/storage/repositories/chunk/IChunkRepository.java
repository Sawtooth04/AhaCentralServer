package com.sawtooth.ahacentralserver.storage.repositories.chunk;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IChunkRepository extends IRepository {
    public Chunk GetByName(String name);

    public int Put(Chunk chunk);

    public List<Chunk> GetByFile(int fileID);

    public List<Chunk> GetByStorageServer(StorageServer server, int start, int count);

    public void Delete(Chunk chunk);
}
