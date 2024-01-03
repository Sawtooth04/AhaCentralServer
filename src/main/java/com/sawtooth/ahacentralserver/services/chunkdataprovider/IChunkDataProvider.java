package com.sawtooth.ahacentralserver.services.chunkdataprovider;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkDownloadModel;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

import java.util.List;

public interface IChunkDataProvider {
    public ChunkDownloadModel TryDownloadChunkFromServer(Chunk chunk, StorageServer server);

    public ChunkDownloadModel TryDownloadChunk(Chunk chunk, List<StorageServer> servers);

    public boolean TryPutChunkToStorageServer(ChunkUploadModel chunkUploadModel, StorageServer server);

    public boolean TryPutChunk(ChunkUploadModel chunkUploadModel, List<StorageServer> servers, int preferredServerPointer);

    public boolean TryDeleteChunkFromStorageServer(Chunk chunk, StorageServer server);

    public boolean TryDeleteChunk(Chunk chunk, List<StorageServer> servers);
}
