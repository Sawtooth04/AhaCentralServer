package com.sawtooth.ahacentralserver.models.storageserver;

public record StorageServer(int storageServerID, String name, String address, int storageServerStatusID) {
    public StorageServer WithStorageServerStatusID(int id) {
        return new StorageServer(storageServerID, name, address, id);
    }
}
