package com.sawtooth.ahacentralserver.storage.repositories.storageserver;

import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

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
}
