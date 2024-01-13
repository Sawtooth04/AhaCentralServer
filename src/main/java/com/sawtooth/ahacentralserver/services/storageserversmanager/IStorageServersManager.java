package com.sawtooth.ahacentralserver.services.storageserversmanager;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServersSpace;

public interface IStorageServersManager {
    public StorageServersSpace GetStorageServersSpace() throws InstantiationException;
}
