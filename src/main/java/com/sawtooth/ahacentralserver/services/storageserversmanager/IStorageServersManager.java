package com.sawtooth.ahacentralserver.services.storageserversmanager;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServersSpace;

import java.util.List;

public interface IStorageServersManager {
    public StorageServersSpace GetStorageServersSpace() throws InstantiationException;

    public List<StorageServer> GetAvailableStorageServers() throws InstantiationException;
}
