package com.sawtooth.ahacentralserver.services.fileupdater;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkTryPutResponse;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.chunkstorageserver.ChunkStorageServer;
import com.sawtooth.ahacentralserver.models.file.File;
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
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class FileUpdater implements IFileUpdater {
    @Value("${file.chunk.size}")
    private int chunkSize;
    private final IStorage storage;
    private final IChunkDataProvider chunkDataProvider;
    private final IChunkNameBuilder chunkNameBuilder;

    public FileUpdater(IStorage storage, IChunkDataProvider chunkDataProvider, IChunkNameBuilder chunkNameBuilder) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
        this.chunkNameBuilder = chunkNameBuilder;
    }

    private boolean IsDifferent(byte[] basic, byte[] check) {
        if (basic == null || basic.length != check.length)
            return true;
        for (int i = 0; i < basic.length; i++)
            if (basic[i] != check[i])
                return true;
        return false;
    }

    private byte[] GetChunkData(Chunk chunk) throws InstantiationException {
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).GetByChunk(chunk);
        return chunkDataProvider.TryDownloadChunk(chunk, servers).getData();
    }

    private boolean TryUpdateChunk(Chunk chunk, byte[] data, List<StorageServer> servers, int serverPointer) throws InstantiationException {
        List<StorageServer> chunkServers = storage.GetRepository(IStorageServerRepository.class).GetByChunk(chunk);
        ChunkUploadModel chunkUploadModel = new ChunkUploadModel(chunk.name(), data);
        ChunkTryPutResponse tryPutResponse;

        if (!chunkDataProvider.TryPutChunk(chunkUploadModel, chunkServers)) {
            if (!(tryPutResponse = chunkDataProvider.TryPutChunk(chunkUploadModel, servers, serverPointer)).result())
                return false;
            else {
                storage.GetRepository(IChunkStorageServerRepository.class).Add(new ChunkStorageServer(-1,
                    chunk.chunkID(), tryPutResponse.server().storageServerID()));
                return true;
            }
        }
        return true;
    }

    private boolean TryDeleteChunk(Chunk chunk) throws InstantiationException {
        List<StorageServer> chunkServers = storage.GetRepository(IStorageServerRepository.class).GetByChunk(chunk);

        if (chunkDataProvider.TryDeleteChunk(chunk, chunkServers)) {
            storage.GetRepository(IChunkRepository.class).Delete(chunk);
            return true;
        }
        return false;
    }

    private boolean PutNewFileChunk(Chunk chunk, byte[] data, List<StorageServer> servers, int serverPointer) throws InstantiationException {
        ChunkUploadModel uploadModel = new ChunkUploadModel(chunk.name(), data);
        ChunkTryPutResponse response = chunkDataProvider.TryPutChunk(uploadModel, servers, serverPointer);
        int chunkID;

        if (response.result()) {
            chunkID = storage.GetRepository(IChunkRepository.class).Put(chunk);
            storage.GetRepository(IChunkRepository.class).Put(chunk);
            storage.GetRepository(IChunkStorageServerRepository.class).Add(new ChunkStorageServer(-1,
                chunkID, response.server().storageServerID()));
            return true;
        }
        return false;
    }

    @Override
    public boolean Update(FileUploadModel model, File file) throws IOException, InstantiationException, NoSuchAlgorithmException {
        List<Chunk> chunks = storage.GetRepository(IChunkRepository.class).GetByFile(file.fileID());
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
        InputStream stream = model.file().getInputStream();
        int chunkPointer = 0, currentServerPointer = 0, read;
        byte[] newChunk = new byte[chunkSize], oldChunk;

        while ((read = stream.read(newChunk, 0, chunkSize)) > 0) {
            if (chunkPointer < chunks.size()) {
                oldChunk = GetChunkData(chunks.get(chunkPointer));
                if (IsDifferent(oldChunk, newChunk))
                    if (!TryUpdateChunk(chunks.get(chunkPointer).WithSize(read), newChunk, servers, currentServerPointer))
                        return false;
            }
            else if (!PutNewFileChunk(new Chunk(-1, file.fileID(), chunkNameBuilder.GetChunkName(file.path(),
                file.name(), chunkPointer), read, chunkPointer), newChunk, servers, currentServerPointer))
                return false;
            chunkPointer++;
            currentServerPointer = (currentServerPointer >= servers.size() - 1) ? 0 : currentServerPointer + 1;
        }
        while (chunkPointer < chunks.size()) {
            if (!TryDeleteChunk(chunks.get(chunkPointer)))
                return false;
            chunkPointer++;
        }
        return true;
    }
}
