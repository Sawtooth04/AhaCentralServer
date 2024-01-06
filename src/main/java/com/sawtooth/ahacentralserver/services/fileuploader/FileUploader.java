package com.sawtooth.ahacentralserver.services.fileuploader;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkTryPutResponse;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.chunkstorageserver.ChunkStorageServer;
import com.sawtooth.ahacentralserver.models.file.FileUploadModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkdataprovider.IChunkDataProvider;
import com.sawtooth.ahacentralserver.services.chunknamebuilder.IChunkNameBuilder;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.chunkstorageserver.IChunkStorageServerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class FileUploader implements IFileUploader {
    @Value("${file.chunk.size}")
    private int chunkSize;
    private final IStorage storage;
    private final IChunkDataProvider chunkDataProvider;
    private final IChunkNameBuilder chunkNameBuilder;

    public FileUploader(IStorage storage, IChunkDataProvider chunkDataProvider, IChunkNameBuilder chunkNameBuilder) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
        this.chunkNameBuilder = chunkNameBuilder;
    }

    private int SaveChunk(int fileID, String name, int size, int sequenceNumber, StorageServer server) throws InstantiationException {
        int chunkID = storage.GetRepository(IChunkRepository.class).Put(new Chunk(-1, fileID, name, size, sequenceNumber));

        SaveChunkToStorageServer(chunkID, server);
        return chunkID;
    }

    private void SaveChunkToStorageServer(int chunkID, StorageServer server) throws InstantiationException {
        storage.GetRepository(IChunkStorageServerRepository.class).Add(new ChunkStorageServer(
            -1, chunkID, server.storageServerID()));
    }

    private void SaveChunkToStorageServers(int chunkID, List<StorageServer> servers) throws InstantiationException {
        for (StorageServer server : servers)
            storage.GetRepository(IChunkStorageServerRepository.class).Add(new ChunkStorageServer(
                -1, chunkID, server.storageServerID()));
    }

    @Override
    public boolean Upload(FileUploadModel model, int fileID) throws IOException, InstantiationException, NoSuchAlgorithmException {
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
        List<StorageServer> backupServers = storage.GetRepository(IStorageServerRepository.class).GetBackup();
        InputStream stream = model.file().getInputStream();
        int currentServerPointer = 0, chunkPointer = 0, read;
        ChunkUploadModel uploadModel;
        ChunkTryPutResponse putResponse;
        byte[] chunk = new byte[chunkSize];

        while ((read = stream.read(chunk, 0, chunkSize)) > 0) {
            uploadModel = new ChunkUploadModel(chunkNameBuilder.GetChunkName(model.path(), model.file().getOriginalFilename(),
                chunkPointer), chunk);
            if (!(putResponse = chunkDataProvider.TryPutChunk(uploadModel, servers, currentServerPointer)).result())
                return false;
            SaveChunkToStorageServers(SaveChunk(fileID, uploadModel.name(), read, chunkPointer, putResponse.server()), backupServers);
            for (StorageServer server : backupServers)
                chunkDataProvider.TryPutChunkToStorageServer(uploadModel, server);
            chunkPointer++;
            currentServerPointer = (currentServerPointer >= servers.size() - 1) ? 0 : currentServerPointer + 1;
        }
        return true;
    }
}
