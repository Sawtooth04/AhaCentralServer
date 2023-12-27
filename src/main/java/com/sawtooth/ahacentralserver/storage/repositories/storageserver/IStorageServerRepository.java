package com.sawtooth.ahacentralserver.storage.repositories.storageserver;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

public interface IStorageServerRepository extends IRepository {
    public void Add(StorageServer storageServer);
}
