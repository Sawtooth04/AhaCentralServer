package com.sawtooth.ahacentralserver.services.chunkdataprovider;

import com.sawtooth.ahacentralserver.models.chunk.*;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

import java.util.List;

public interface IChunkDataProvider {
    public ChunkDownloadModel TryDownloadChunkFromServer(Chunk chunk, StorageServer server);

    public ChunkDownloadModel TryDownloadChunk(Chunk chunk, List<StorageServer> servers);

    public boolean TryPutChunkToStorageServer(ChunkUploadModel chunkUploadModel, StorageServer server);

    public ChunkTryPutResponse TryPutChunk(ChunkUploadModel chunkUploadModel, List<StorageServer> servers, int preferredServerPointer);

    public boolean TryPutChunk(ChunkUploadModel chunkUploadModel, List<StorageServer> servers);

    public boolean TryDeleteChunkFromStorageServer(Chunk chunk, StorageServer server);

    public boolean TryDeleteChunk(Chunk chunk, List<StorageServer> servers);

    public ChunkLastModified TryGetLastModifiedTimestampFromStorageServer(Chunk chunk, StorageServer server);

    public ChunkLastModified TryGetLastModifiedTimestamp(Chunk chunk, List<StorageServer> servers);
}
