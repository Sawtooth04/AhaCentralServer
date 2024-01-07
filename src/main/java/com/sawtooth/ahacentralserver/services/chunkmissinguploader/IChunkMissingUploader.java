package com.sawtooth.ahacentralserver.services.chunkmissinguploader;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

public interface IChunkMissingUploader {
    public boolean UploadMissing(StorageServer server) throws InstantiationException;
}
