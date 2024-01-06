package com.sawtooth.ahacentralserver.models.chunk;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

public record ChunkTryPutResponse(boolean result, StorageServer server) {
}
