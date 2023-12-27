package com.sawtooth.ahacentralserver.storage.repositories.storageserverstatus;

import com.sawtooth.ahacentralserver.models.storageserverstatus.StorageServerStatus;
import com.sawtooth.ahacentralserver.models.storageserverstatus.StorageServerStatusMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class StorageServerStatusRepository implements IStorageServerStatusRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public StorageServerStatus Get(String name) {
        return template.queryForObject("SELECT * FROM get_storage_server_status(?)", new StorageServerStatusMapper(), name);
    }
}
