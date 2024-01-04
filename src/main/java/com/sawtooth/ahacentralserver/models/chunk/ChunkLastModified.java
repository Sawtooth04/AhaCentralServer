package com.sawtooth.ahacentralserver.models.chunk;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

public record ChunkLastModified(long lastModified, StorageServer server) {
}
