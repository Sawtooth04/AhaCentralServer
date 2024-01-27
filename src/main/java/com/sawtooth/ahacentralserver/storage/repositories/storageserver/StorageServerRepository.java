package com.sawtooth.ahacentralserver.storage.repositories.storageserver;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;
import java.util.Objects;

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
    public StorageServer Get(int storageServerID) {
        return template.queryForObject("SELECT * FROM get_storage_server_by_id(?)", new StorageServerMapper(), storageServerID);
    }

    @Override
    public List<StorageServer> GetBackup() {
        return template.query("SELECT * FROM get_backup_servers()", new StorageServerMapper());
    }

    @Override
    public StorageServer Get(String address) {
        return template.queryForObject("SELECT * FROM get_storage_server(?)", new StorageServerMapper(), address);
    }

    @Override
    public List<StorageServer> GetByChunk(Chunk chunk) {
        return template.query("SELECT * FROM get_storage_servers_by_chunk(?)", new StorageServerMapper(), chunk.chunkID());
    }

    @Override
    public void Delete(int storageServerID) {
        template.queryForObject("SELECT * FROM delete_storage_server(?)", new SingleColumnRowMapper<>(), storageServerID);
    }

    @Override
    public int StorageCount() {
        return Objects.requireNonNull(template.queryForObject("SELECT * FROM get_storage_servers_count()",
            new SingleColumnRowMapper<>()));
    }

    @Override
    public int BackupCount() {
        return Objects.requireNonNull(template.queryForObject("SELECT * FROM get_backup_servers_count()",
            new SingleColumnRowMapper<>()));
    }
}
