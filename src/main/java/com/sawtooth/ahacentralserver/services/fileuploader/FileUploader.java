package com.sawtooth.ahacentralserver.services.fileuploader;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahacentralserver.models.file.FilePutModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
public class FileUploader implements IFileUploader {
    @Value("${file.chunk.size}")
    private int chunkSize;
    private final WebClient webClient;
    private final IStorage storage;

    public FileUploader(WebClient webClient, IStorage storage) {
        this.webClient = webClient;
        this.storage = storage;
    }

    private String GetChunkName(FilePutModel model, int chunkPointer) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(String.join("/", model.path(), model.file().getOriginalFilename(),
            Integer.toString(chunkPointer)).getBytes(StandardCharsets.UTF_8));
        return (new String(Base64.getEncoder().encode(hash))).replaceAll("[/\\?%*:|\"<>.,;]", "");
    }

    private void SaveChunk(int fileID, String name, int size, int sequenceNumber) throws InstantiationException {
        storage.GetRepository(IChunkRepository.class).Put(new Chunk(-1, fileID, name, size, sequenceNumber));
    }

    private void SendChunkToStorageServer(ChunkUploadModel chunkUploadModel, StorageServer server) {
        RepresentationModel<?> links = webClient.get().uri(String.join("", server.address(), "/api/"))
            .retrieve().bodyToMono(RepresentationModel.class).block();

        if (links != null && links.getLink("chunk-put").isPresent())
            webClient.put().uri(String.join("", server.address(), links.getLink("chunk-put").get().getHref()))
                .bodyValue(chunkUploadModel).retrieve().bodyToMono(RepresentationModel.class).block();
    }

    @Override
    public void Upload(FilePutModel model, int fileID) throws IOException, InstantiationException, NoSuchAlgorithmException {
        List<StorageServer> servers = storage.GetRepository(IStorageServerRepository.class).Get();
        InputStream stream = model.file().getInputStream();
        int currentServerPointer = 0, chunkPointer = 0, read;
        ChunkUploadModel uploadModel;
        byte[] chunk = new byte[chunkSize];

        while ((read = stream.read(chunk, 0, chunkSize)) > 0) {
            uploadModel = new ChunkUploadModel(GetChunkName(model, chunkPointer), chunk);
            SendChunkToStorageServer(uploadModel, servers.get(currentServerPointer));
            SaveChunk(fileID, uploadModel.name(), read, chunkPointer);
            chunkPointer++;
            currentServerPointer = (currentServerPointer >= servers.size() - 1) ? 0 : currentServerPointer + 1;
        }
    }
}
