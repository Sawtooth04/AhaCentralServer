package com.sawtooth.ahacentralserver.services.filedeleter;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkdataprovider.IChunkDataProvider;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileDeleter implements IFileDeleter {
    private final IStorage storage;
    private final IChunkDataProvider chunkDataProvider;

    public FileDeleter(IStorage storage, IChunkDataProvider chunkDataProvider) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
    }

    private boolean DeleteChunk(Chunk chunk, List<StorageServer> servers) throws InstantiationException {
        boolean result = chunkDataProvider.TryDeleteChunk(chunk, servers);

        storage.GetRepository(IChunkRepository.class).Delete(chunk);
        return result;
    }

    @Override
    public boolean Delete(File file) throws InstantiationException {
        boolean result = true;
        List<Chunk> chunks = storage.GetRepository(IChunkRepository.class).GetByFile(file.fileID());
        IStorageServerRepository storageServerRepository = storage.GetRepository(IStorageServerRepository.class);

        for (Chunk chunk : chunks)
            result &= DeleteChunk(chunk, storageServerRepository.GetByChunk(chunk));
        return result;
    }
}
