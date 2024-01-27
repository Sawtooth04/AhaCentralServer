package com.sawtooth.ahacentralserver.storage.repositories.storageserver;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IStorageServerRepository extends IRepository {
    public void Add(StorageServer storageServer);

    public List<StorageServer> Get();

    public StorageServer Get(int storageServerID);

    public List<StorageServer> GetBackup();

    public StorageServer Get(String address);

    public List<StorageServer> GetByChunk(Chunk chunk);

    public void Delete(int storageServerID);

    public int StorageCount();

    public int BackupCount();
}
