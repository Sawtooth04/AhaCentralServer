package com.sawtooth.ahacentralserver.storage.repositories.storageserver;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

public class StorageServerRepository implements IStorageServerRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void Add(StorageServer storageServer) {
        template.queryForObject("SELECT * FROM add_storage_server(?, ?, ?)", new SingleColumnRowMapper<>(),
            storageServer.name(), storageServer.address(), storageServer.storageServerStatusID());
    }

    @Override
    public List<StorageServer> Get() {
        return template.query("SELECT * FROM get_storage_servers()", new StorageServerMapper());
    }

    @Override
    public List<StorageServer> GetByChunk(Chunk chunk) {
        return template.query("SELECT * FROM get_storage_servers_by_chunk(?)", new StorageServerMapper(), chunk.chunkID());
    }
}
