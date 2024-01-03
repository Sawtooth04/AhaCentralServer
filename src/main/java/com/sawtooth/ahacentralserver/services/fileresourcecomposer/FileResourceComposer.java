package com.sawtooth.ahacentralserver.services.fileresourcecomposer;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkDownloadModel;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.services.chunkdataprovider.IChunkDataProvider;
import com.sawtooth.ahacentralserver.services.uribuilder.IUriBuilder;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileResourceComposer implements IFileResourceComposer {
    @Value("${sys.temp.folder}")
    private String tempFolder;
    private final IStorage storage;
    private final IChunkDataProvider chunkDataProvider;

    @Autowired
    public FileResourceComposer(IStorage storage, IChunkDataProvider chunkDataProvider) {
        this.storage = storage;
        this.chunkDataProvider = chunkDataProvider;
    }

    private boolean TryDownloadChunk(java.io.File tempFile, Chunk chunk, List<StorageServer> servers) throws IOException {
        ChunkDownloadModel chunkDownloadModel = chunkDataProvider.TryDownloadChunk(chunk, servers);

        if (chunkDownloadModel != null) {
            try (FileOutputStream output = new FileOutputStream(tempFile, true)) {
                output.write(chunkDownloadModel.getData(), 0, chunk.size());
            }
            return true;
        }
        return false;
    }

    @Override
    public java.io.File Compose(File file) throws InstantiationException, IOException {
        List<Chunk> chunks = storage.GetRepository(IChunkRepository.class).GetByFile(file.fileID());
        int sequenceNumber = 0;
        Path root = Path.of(String.join("", tempFolder, file.path()));
        java.io.File tempFile = new java.io.File(String.join("/", root.toString(), file.name()));

        if (!tempFile.delete()) {
            Files.createDirectories(root);
            Files.createFile(tempFile.toPath());
        }
        do {
            if (!TryDownloadChunk(tempFile, chunks.get(sequenceNumber), storage.GetRepository(IStorageServerRepository.class)
                .GetByChunk(chunks.get(sequenceNumber))))
                return null;
            sequenceNumber++;
        }
        while (sequenceNumber < chunks.size());
        return tempFile;
    }
}
