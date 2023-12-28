package com.sawtooth.ahacentralserver.models.storageservercondition;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

public record StorageServerCondition(int storageServerID, String address, long allocatedMemory, long usedMemory) {
    public StorageServerCondition WithStorageServer(StorageServer storageServer) {
        return new StorageServerCondition(
            storageServer.storageServerID(),
            storageServer.address(),
            allocatedMemory,
            usedMemory
        );
    }
}
