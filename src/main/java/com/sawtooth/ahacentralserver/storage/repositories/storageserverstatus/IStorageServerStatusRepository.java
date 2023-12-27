package com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus;

import com.sawtooth.ahacentralserver.models.storageserverstatus.StorageServerStatus;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

public interface IStorageServerStatusRepository extends IRepository {
    public StorageServerStatus Get(String name);
}
