package com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus;

import com.sawtooth.ahacentralserver.models.storageserverstatus.StorageServerStatus;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IStorageServerStatusRepository extends IRepository {
    public List<StorageServerStatus> Get();

    public StorageServerStatus Get(String name);
}
