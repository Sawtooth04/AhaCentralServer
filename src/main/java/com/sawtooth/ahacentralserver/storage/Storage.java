package com.sawtooth.ahacentralserver.storage;

import com.sawtooth.ahacentralserver.storage.repositories.IRepository;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.ChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.chunk.IChunkRepository;
import com.sawtooth.ahacentralserver.storage.repositories.chunkstorageserver.ChunkStorageServerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.chunkstorageserver.IChunkStorageServerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.customer.CustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.file.FileRepository;
import com.sawtooth.ahacentralserver.storage.repositories.file.IFileRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.IStorageServerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserver.StorageServerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus.IStorageServerStatusRepository;
import com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus.StorageServerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Storage implements IStorage {
    private static HashMap<String, String> repositories;
    private final JdbcTemplate template;

    @Autowired
    public Storage(JdbcTemplate template) {
        if (repositories == null) {
            repositories = new HashMap<>();
            Initialization();
        }
        this.template = template;
    }

    private static void Initialization() {
        repositories.put(IStorageServerRepository.class.getName(), StorageServerRepository.class.getName());
        repositories.put(IStorageServerStatusRepository.class.getName(), StorageServerStatusRepository.class.getName());
        repositories.put(ICustomerRepository.class.getName(), CustomerRepository.class.getName());
        repositories.put(IFileRepository.class.getName(), FileRepository.class.getName());
        repositories.put(IChunkRepository.class.getName(), ChunkRepository.class.getName());
        repositories.put(IChunkStorageServerRepository.class.getName(), ChunkStorageServerRepository.class.getName());
    }

    public <T extends IRepository> T GetRepository(Class<T> interfaceObject) throws InstantiationException{
        try {
            String repositoryName = repositories.get(interfaceObject.getName());
            T repository = (T) Class.forName(repositoryName).newInstance();
            repository.SetJbdcTemplate(template);
            return repository;
        }
        catch (Exception exception) {
            throw new InstantiationException(exception.getMessage());
        }
    }
}
