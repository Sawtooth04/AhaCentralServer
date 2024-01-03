package com.sawtooth.ahacentralserver.services.fileuploader;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.chunkstorageserver.ChunkStorageServer;
import com.sawtooth.ahacentralserver.models.file.FileUploadModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkdataprovider.IChunkDataProvider;
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

    public FileUploader(IStorage storage, IChunkDataProvider chunkDataProvider) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
    }

    private String GetChunkName(FileUploadModel model, int chunkPointer) throws NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(String.join("/", model.path(), model.file().getOriginalFilename(),
            Integer.toString(chunkPointer)).getBytes(StandardCharsets.UTF_8));
        for (byte value : hash)
            stringBuilder.append(value);
        return stringBuilder.toString();
    }

    private void SaveChunk(int fileID, String name, int size, int sequenceNumber, StorageServer server) throws InstantiationException {
        int chunkID = storage.GetRepository(IChunkRepository.class).Put(new Chunk(-1, fileID, name, size, sequenceNumber));
        storage.GetRepository(IChunkStorageServerRepository.class).Add(new ChunkStorageServer(
            -1, chunkID, server.storageServerID()));
    }

    @Override
    public boolean Upload(FileUploadModel model, int fileID) throws IOException, InstantiationException, NoSuchAlgorithmException {
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
        InputStream stream = model.file().getInputStream();
        int currentServerPointer = 0, chunkPointer = 0, read;
        ChunkUploadModel uploadModel;
        byte[] chunk = new byte[chunkSize];

        while ((read = stream.read(chunk, 0, chunkSize)) > 0) {
            uploadModel = new ChunkUploadModel(GetChunkName(model, chunkPointer), chunk);
            if (!chunkDataProvider.TryPutChunk(uploadModel, servers, currentServerPointer))
                return false;
            SaveChunk(fileID, uploadModel.name(), read, chunkPointer, servers.get(currentServerPointer));
            chunkPointer++;
            currentServerPointer = (currentServerPointer >= servers.size() - 1) ? 0 : currentServerPointer + 1;
        }
        return true;
    }
}
