package com.sawtooth.ahacentralserver.services.fileupdater;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.file.FileUploadModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkdataprovider.IChunkDataProvider;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileUpdater implements IFileUpdater {
    @Value("${file.chunk.size}")
    private int chunkSize;
    private final IStorage storage;
    private final IChunkDataProvider chunkDataProvider;

    public FileUpdater(IStorage storage, IChunkDataProvider chunkDataProvider) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
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

    private boolean SaveChunk(Chunk chunk, byte[] data, List<StorageServer> servers, int preferredServerPointer) throws InstantiationException {
        if (chunkDataProvider.TryPutChunk(new ChunkUploadModel(chunk.name(), data), servers, preferredServerPointer)) {
            storage.GetRepository(IChunkRepository.class).Put(chunk);
            return true;
        }
        return false;
    }

    @Override
    public boolean Update(FileUploadModel model, File file) throws IOException, InstantiationException {
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
        List<Chunk> chunks = storage.GetRepository(IChunkRepository.class).GetByFile(file.fileID());
        InputStream stream = model.file().getInputStream();
        int chunkPointer = 0, currentServerPointer = 0, read;
        byte[] newChunk = new byte[chunkSize], oldChunk;

        while ((read = stream.read(newChunk, 0, chunkSize)) > 0) {
            oldChunk = GetChunkData(chunks.get(chunkPointer));
            if (IsDifferent(oldChunk, newChunk))
                if (!SaveChunk(chunks.get(chunkPointer).WithSize(read), newChunk, servers, currentServerPointer))
                    return false;
            chunkPointer++;
            currentServerPointer = (currentServerPointer >= servers.size() - 1) ? 0 : currentServerPointer + 1;
        }
        return true;
    }
}
