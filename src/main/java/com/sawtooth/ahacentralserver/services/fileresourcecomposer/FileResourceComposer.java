package com.sawtooth.ahacentralserver.services.fileresourcecomposer;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkDownloadModel;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
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
    private final WebClient webClient;
    private final IUriBuilder uriBuilder;

    @Autowired
    public FileResourceComposer(IStorage storage, WebClient webClient, IUriBuilder uriBuilder) {
        this.storage = storage;
        this.webClient = webClient;
        this.uriBuilder = uriBuilder;
    }

    private ChunkDownloadModel TryDownloadChunkFromServer(Chunk chunk, StorageServer server) {
        RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
            .retrieve().bodyToMono(RepresentationModel.class).block();

        if (links != null && links.getLink("chunk-get").isPresent())
            try {
                return webClient.get().uri(uriBuilder
                    .Path(String.join("", server.address(), links.getLink("chunk-get").orElseThrow().getHref()))
                    .Param("name", chunk.name())
                    .Uri()).retrieve().bodyToMono(ChunkDownloadModel.class).block();
            }
            catch (Exception exception) {
                return null;
            }
        return null;
    }

    private boolean TryDownloadChunk(java.io.File tempFile, Chunk chunk, List<StorageServer> servers) throws IOException {
        ChunkDownloadModel chunkDownloadModel;

        for (StorageServer server : servers)
            if ((chunkDownloadModel = TryDownloadChunkFromServer(chunk, server)) != null) {
                try (FileOutputStream output = new FileOutputStream(tempFile, true)) {
                    output.write(chunkDownloadModel.getData(), 0, chunk.size());
                }
                return true;
            }
        return false;
    }

    private List<Chunk> GetChunksBySequenceNumber(List<Chunk> chunks, int sequenceNumber) {
        return chunks.stream().filter(chunk -> chunk.sequenceNumber() == sequenceNumber).collect(Collectors.toList());
    }

    @Override
    public boolean Compose(File file) throws InstantiationException, IOException {
        List<Chunk> chunks = storage.GetRepository(IChunkRepository.class).GetByFile(file.fileID());
        List<Chunk> sequenceChunks;
        int sequenceNumber = 0;
        Path root = Path.of(String.join("", tempFolder, file.path()));
        java.io.File tempFile = new java.io.File(String.join("/", root.toString(), file.name()));

        if (!tempFile.delete()) {
            Files.createDirectories(root);
            Files.createFile(tempFile.toPath());
        }
        do {
            sequenceChunks = GetChunksBySequenceNumber(chunks, sequenceNumber);
            for (Chunk chunk : sequenceChunks)
                if (!TryDownloadChunk(tempFile, chunk, storage.GetRepository(IStorageServerRepository.class).GetByChunk(chunk)))
                    return false;
            sequenceNumber++;
        }
        while (!sequenceChunks.isEmpty());
        return true;
    }
}
